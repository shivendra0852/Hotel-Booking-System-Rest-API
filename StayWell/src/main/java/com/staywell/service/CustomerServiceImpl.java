package com.staywell.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.staywell.dto.CustomerDTO;
import com.staywell.enums.Role;
import com.staywell.exception.CustomerException;
import com.staywell.model.Customer;
import com.staywell.repository.CustomerDao;
import com.staywell.repository.HotelDao;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerDao customerDao;

	@Autowired
	private HotelDao hotelDao;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public Customer registerCustomer(CustomerDTO customerDTO) throws CustomerException {

		if (isEmailExists(customerDTO.getEmail())) {
			throw new CustomerException("Customer is already exist ...!");
		}

		Customer customer = buildCustomer(customerDTO);
		return customerDao.save(customer);

	}

	@Override
	public Customer updateCustomer(CustomerDTO customerDto) throws CustomerException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();

		System.out.println(auth);

		Optional<Customer> customerExist = customerDao.findByEmail(email);

		if (customerExist.isPresent()) {
			Customer customer = customerExist.get();

			if (customerDto.getName() != null) {
				customer.setName(customerDto.getName());
			}
			if (customerDto.getGender() != null) {
				customer.setGender(customerDto.getGender());
			}
			if (customerDto.getDob() != null) {
				customer.setDob(customerDto.getDob());
			}
			if (customerDto.getAddress() != null) {
				customer.setAddress(customerDto.getAddress());
			}
			if (customerDto.getEmail() != null) {
				customer.setEmail(customerDto.getEmail());
			}
			if (customerDto.getPhone() != null) {
				customer.setPhone(customerDto.getPhone());
			}
			if (customerDto.getPassword() != null) {
				customer.setPassword(passwordEncoder.encode(customerDto.getPassword()));
			}

			return customerDao.save(customer);
		} else {
			throw new CustomerException("Customer does not exist");
		}
	}

	public Customer deleteCustomer(Authentication authentication) throws CustomerException {
		String email = authentication.getName();
		Optional<Customer> customerExist = customerDao.findByEmail(email);

		if (customerExist.isPresent()) {
			Customer customer = customerExist.get();
			customerDao.delete(customer);
			return customer;
		} else {
			throw new CustomerException("Customer does not exist");
		}
	}

	@Override
	public List<Customer> getAllCustomer() throws CustomerException {

		List<Customer> customers = customerDao.findAll();

		if (!customers.isEmpty()) {

			return customers;
		}

		else
			throw new CustomerException("Customers not exist");
	}

	@Override
	public Customer getCustomerById(Long customerId) throws CustomerException {

		Optional<Customer> customerExist = customerDao.findById(customerId);

		if (customerExist.isPresent()) {

			Customer customer = customerExist.get();

			return customer;
		}

		else
			throw new CustomerException("Customer not exist by this Id : " + customerId);
	}

	private boolean isEmailExists(String email) {
		return customerDao.findByEmail(email).isPresent() || hotelDao.findByHotelEmail(email).isPresent();
	}

	private Customer buildCustomer(CustomerDTO customerDTO) {
		return Customer.builder().name(customerDTO.getName()).email(customerDTO.getEmail())
				.password(passwordEncoder.encode(customerDTO.getPassword())).phone(customerDTO.getPhone())
				.gender(customerDTO.getGender()).registrationDateTime(LocalDateTime.now()).role(Role.ROLE_CUSTOMER)
				.dob(customerDTO.getDob()).address(customerDTO.getAddress()).build();
	}

}
