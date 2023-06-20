package com.staywell.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.staywell.dto.request.CustomerRequest;
import com.staywell.dto.request.UpdateRequest;
import com.staywell.enums.Role;
import com.staywell.exception.CustomerException;
import com.staywell.model.Customer;
import com.staywell.model.DeleteReason;
import com.staywell.model.Reservation;
import com.staywell.repository.CustomerDao;
import com.staywell.repository.DeleteReasonDao;
import com.staywell.repository.HotelDao;
import com.staywell.service.CustomerService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
@Transactional
public class CustomerServiceImpl implements CustomerService {

	private CustomerDao customerDao;
	private HotelDao hotelDao;
	private PasswordEncoder passwordEncoder;
	private DeleteReasonDao deleteReasonDao;

	@Override
	public Customer registerCustomer(CustomerRequest customerRequest) {
		log.info("Performing email validation");
		if (isEmailExists(customerRequest.getEmail())) {
			throw new CustomerException("Customer is already exist ...!");
		}

		Customer customer = buildCustomer(customerRequest);
		customerDao.save(customer);

		log.info("Registration successfull");
		return customer;
	}

	@Override
	public String updateName(UpdateRequest updateRequest) {
		Customer currentCustomer = getCurrentLoggedInCustomer();

		log.info("Verifying credentials");
		String password = updateRequest.getPassword();
		if (!passwordEncoder.matches(password, currentCustomer.getPassword())) {
			throw new CustomerException("Wrong credentials!");
		}
		customerDao.setCustomerEmail(currentCustomer.getCustomerId(), updateRequest.getField());

		log.info("Updation successfull");
		return "Name updated successfully!";
	}

	@Override
	public String updatePassword(UpdateRequest updateRequest) {
		Customer currentCustomer = getCurrentLoggedInCustomer();

		log.info("Verifying credentials");
		String password = updateRequest.getPassword();
		if (!passwordEncoder.matches(password, currentCustomer.getPassword())) {
			throw new CustomerException("Wrong credentials!");
		}
		customerDao.setCustomerPassword(currentCustomer.getCustomerId(),
				passwordEncoder.encode(updateRequest.getField()));

		log.info("Updation successfull");
		return "Password updated successfully!";
	}

	@Override
	public String updatePhone(UpdateRequest updateRequest) {
		Customer currentCustomer = getCurrentLoggedInCustomer();

		log.info("Verifying credentials");
		String password = updateRequest.getPassword();
		if (!passwordEncoder.matches(password, currentCustomer.getPassword())) {
			throw new CustomerException("Wrong credentials!");
		}
		customerDao.setCustomerPhone(currentCustomer.getCustomerId(), updateRequest.getField());

		log.info("Updation successfull");
		return "Phone updated successfully!";
	}

	@Override
	public String deleteCustomer(UpdateRequest updateRequest) {
		Customer currentCustomer = getCurrentLoggedInCustomer();

		log.info("Verifying credentials");
		String password = updateRequest.getPassword();
		if (!passwordEncoder.matches(password, currentCustomer.getPassword())) {
			throw new CustomerException("Wrong credentials!");
		}

		List<Reservation> reservations = currentCustomer.getReservations();
		List<Reservation> pendingReservations = reservations.stream().filter(
				r -> r.getCheckoutDate().isAfter(LocalDate.now()) || r.getCheckoutDate().isEqual(LocalDate.now()))
				.collect(Collectors.toList());

		log.info("Checking for any pending reservations");
		if (!pendingReservations.isEmpty())
			throw new CustomerException("Pending reservations! Account can't be deleted");

		log.info("Setting Account status to be deleted");
		currentCustomer.setToBeDeleted(true);
		currentCustomer.setDeletionScheduledAt(LocalDateTime.now());
		customerDao.save(currentCustomer);

		DeleteReason deleteReason = new DeleteReason();
		deleteReason.setReason(updateRequest.getField());
		deleteReasonDao.save(deleteReason);

		log.info("Account schelduled for deletion and Logged out!");
		return "Account schelduled for deletion. To recovered loggin again within 24 hours";
	}

	@Override
	public List<Customer> getToBeDeletedCustomers() {
		List<Customer> customers = customerDao.findByToBeDeleted(true);
		if (customers.isEmpty())
			throw new CustomerException("No accounts found for deletion");
		return customers;
	}

	@Override
	public Customer getCustomerById(Long customerId) {
		Optional<Customer> optional = customerDao.findById(customerId);
		if (optional.isEmpty())
			throw new CustomerException("Customer not exist by this Id : " + customerId);
		return optional.get();
	}

	private Customer getCurrentLoggedInCustomer() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		return customerDao.findByEmail(email).get();
	}

	private boolean isEmailExists(String email) {
		return customerDao.findByEmail(email).isPresent() || hotelDao.findByHotelEmail(email).isPresent();
	}

	private Customer buildCustomer(CustomerRequest customerRequest) {
		return Customer.builder()
				.name(customerRequest.getName())
				.email(customerRequest.getEmail()).password(passwordEncoder.encode(customerRequest.getPassword()))
				.phone(customerRequest.getPhone())
				.gender(customerRequest.getGender())
				.registrationDateTime(LocalDateTime.now())
				.role(Role.ROLE_CUSTOMER)
				.dob(customerRequest.getDob())
				.address(customerRequest.getAddress())
				.toBeDeleted(false)
				.reservations(new ArrayList<>())
				.build();
	}

}
