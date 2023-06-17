package com.staywell.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.staywell.dto.CustomerDTO;
import com.staywell.dto.UpdateDetailsDTO;
import com.staywell.enums.Role;
import com.staywell.exception.CustomerException;
import com.staywell.exception.HotelException;
import com.staywell.model.Customer;
import com.staywell.model.Reservation;
import com.staywell.repository.CustomerDao;
import com.staywell.repository.HotelDao;
import com.staywell.service.CustomerService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {

	private CustomerDao customerDao;
	private HotelDao hotelDao;
	private PasswordEncoder passwordEncoder;

	@Override
	public Customer registerCustomer(CustomerDTO customerDTO) throws CustomerException {
		log.info("Performing email validation");
		if (isEmailExists(customerDTO.getEmail())) {
			throw new CustomerException("Customer is already exist ...!");
		}

		Customer customer = buildCustomer(customerDTO);
		customerDao.save(customer);

		log.info("Registration successfull");
		return customer;
	}

	@Override
	public String updateName(UpdateDetailsDTO updateRequest) {
		Customer currentCustomer = getCurrentLoggedInCustomer();

		log.info("Verifying credentials");
		String password = updateRequest.getPassword();
		if (!passwordEncoder.matches(password, currentCustomer.getPassword())) {
			throw new HotelException("Wrong credentials!");
		}
		customerDao.setCustomerEmail(currentCustomer.getCustomerId(), updateRequest.getField());

		log.info("Updation successfull");
		return "Name updated successfully!";
	}

	@Override
	public String updatePassword(UpdateDetailsDTO updateRequest) {
		Customer currentCustomer = getCurrentLoggedInCustomer();

		log.info("Verifying credentials");
		String password = updateRequest.getPassword();
		if (!passwordEncoder.matches(password, currentCustomer.getPassword())) {
			throw new HotelException("Wrong credentials!");
		}
		customerDao.setCustomerPassword(currentCustomer.getCustomerId(),
				passwordEncoder.encode(updateRequest.getField()));

		log.info("Updation successfull");
		return "Password updated successfully!";
	}

	@Override
	public String updatePhone(UpdateDetailsDTO updateRequest) {
		Customer currentCustomer = getCurrentLoggedInCustomer();

		log.info("Verifying credentials");
		String password = updateRequest.getPassword();
		if (!passwordEncoder.matches(password, currentCustomer.getPassword())) {
			throw new HotelException("Wrong credentials!");
		}
		customerDao.setCustomerPhone(currentCustomer.getCustomerId(), updateRequest.getField());

		log.info("Updation successfull");
		return "Phone updated successfully!";
	}

	public String deleteCustomer() throws CustomerException {
		Customer currentCustomer = getCurrentLoggedInCustomer();
		List<Reservation> reservations = currentCustomer.getReservations();
		List<Reservation> pendingReservations = reservations.stream().filter(
				r -> r.getCheckoutDate().isAfter(LocalDate.now()) || r.getCheckoutDate().isEqual(LocalDate.now()))
				.collect(Collectors.toList());

		log.info("Checking for any pending reservations");
		if (!pendingReservations.isEmpty())
			throw new CustomerException("Pending reservations! Account can't be deleted");

		log.info("Setting Account status to be deleted");
		currentCustomer.setToBeDeleted(true);
		customerDao.save(currentCustomer);

		log.info("Account schelduled for deletion and Logged out!");
		return "Account schelduled for deletion. To recovered loggin again within 24 hours";
	}

	@Override
	public List<Customer> getToBeDeletedCustomers() throws CustomerException {
		List<Customer> customers = customerDao.findByToBeDeleted(true);
		if (!customers.isEmpty())
			throw new CustomerException("Customers not exist");
		return customers;
	}

	@Override
	public Customer getCustomerById(Long customerId) throws CustomerException {
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

	private Customer buildCustomer(CustomerDTO customerDTO) {
		return Customer.builder()
				.name(customerDTO.getName())
				.email(customerDTO.getEmail())
				.password(passwordEncoder.encode(customerDTO.getPassword()))
				.phone(customerDTO.getPhone())
				.gender(customerDTO.getGender())
				.registrationDateTime(LocalDateTime.now())
				.role(Role.ROLE_CUSTOMER)
				.dob(customerDTO.getDob())
				.address(customerDTO.getAddress())
				.build();
	}

}
