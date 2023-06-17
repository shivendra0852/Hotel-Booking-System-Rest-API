package com.staywell.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.staywell.dto.CustomerDTO;
import com.staywell.dto.UpdateDetailsDTO;
import com.staywell.model.Customer;
import com.staywell.service.CustomerService;

@RestController
@RequestMapping("/staywell/customers")
public class CustomerController {

	@Autowired
	private CustomerService customerService;

	@PostMapping("/register")
	public ResponseEntity<Customer> registerCustomer(@RequestBody CustomerDTO customerDTO) {
		Customer res = customerService.registerCustomer(customerDTO);
		return new ResponseEntity<Customer>(res, HttpStatus.CREATED);
	}

	@PutMapping("/update-name")
	public ResponseEntity<String> updateName(@RequestBody UpdateDetailsDTO updateRequest) {
		String res = customerService.updateName(updateRequest);
		return new ResponseEntity<>(res, HttpStatus.OK);

	}

	@PutMapping("/update-password")
	public ResponseEntity<String> updatePassword(@RequestBody UpdateDetailsDTO updateRequest) {
		String res = customerService.updateName(updateRequest);
		return new ResponseEntity<>(res, HttpStatus.OK);

	}

	@PutMapping("/update-phone")
	public ResponseEntity<String> updatePhone(@RequestBody UpdateDetailsDTO updateRequest) {
		String res = customerService.updateName(updateRequest);
		return new ResponseEntity<>(res, HttpStatus.OK);

	}

	@DeleteMapping("/delete")
	public ResponseEntity<String> deleteCustomer() {
		String res = customerService.deleteCustomer();
		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	@GetMapping("/getAllCustomer")
	public ResponseEntity<List<Customer>> getAllCustomer() {
		List<Customer> res = customerService.getToBeDeletedCustomers();
		return new ResponseEntity<List<Customer>>(res, HttpStatus.OK);
	}

	@GetMapping("/getCustomerById/{customerId}")
	public ResponseEntity<Customer> getCustomerById(@PathVariable("customerId") Long customerId) {
		Customer res = customerService.getCustomerById(customerId);
		return new ResponseEntity<Customer>(res, HttpStatus.OK);
	}

}
