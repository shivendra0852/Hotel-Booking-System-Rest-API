package com.staywell.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.staywell.dto.CustomerDTO;
import com.staywell.model.Customer;
import com.staywell.service.CustomerService;

@RestController
@RequestMapping("/staywell/customer")
public class CustomerController {

	@Autowired
	private CustomerService cService;

	@PostMapping("/register")
	public ResponseEntity<Customer> createCustomer(@RequestBody CustomerDTO customerDTO) {

		Customer res = cService.registerCustomer(customerDTO);

		return new ResponseEntity<Customer>(res, HttpStatus.CREATED);
	}

	@PutMapping("/update")
	public ResponseEntity<Customer> updateCustomer(@RequestBody CustomerDTO customerDto) {

		Customer res = cService.updateCustomer(customerDto);

		return new ResponseEntity<Customer>(res, HttpStatus.OK);

	}

	@DeleteMapping("/delete")
	public ResponseEntity<Customer> deleteCustomer(Authentication authentication) {

		Customer res = cService.deleteCustomer(authentication);

		return new ResponseEntity<Customer>(res, HttpStatus.OK);
	}

	@GetMapping("/getAllCustomer")
	public ResponseEntity<List<Customer>> getAllCustomer() {

		List<Customer> res = cService.getAllCustomer();

		return new ResponseEntity<List<Customer>>(res, HttpStatus.OK);
	}

	@GetMapping("/getCustomerById/{customerId}")
	public ResponseEntity<Customer> getCustomerById(@PathVariable("customerId") Long customerId) {

		Customer res = cService.getCustomerById(customerId);

		return new ResponseEntity<Customer>(res, HttpStatus.OK);
	}

}
