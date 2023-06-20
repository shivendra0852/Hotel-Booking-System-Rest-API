package com.staywell.controller;

import java.util.List;

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

import com.staywell.dto.request.CustomerRequest;
import com.staywell.dto.request.UpdateRequest;
import com.staywell.model.Customer;
import com.staywell.service.CustomerService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/staywell/customers")
@AllArgsConstructor
public class CustomerController {

	private CustomerService customerService;

	@PostMapping("/register")
	public ResponseEntity<Customer> registerCustomer(@RequestBody CustomerRequest customerRequest) {
		Customer res = customerService.registerCustomer(customerRequest);
		return new ResponseEntity<Customer>(res, HttpStatus.CREATED);
	}

	@PutMapping("/update-name")
	public ResponseEntity<String> updateName(@RequestBody UpdateRequest updateRequest) {
		String res = customerService.updateName(updateRequest);
		return new ResponseEntity<>(res, HttpStatus.OK);

	}

	@PutMapping("/update-password")
	public ResponseEntity<String> updatePassword(@RequestBody UpdateRequest updateRequest) {
		String res = customerService.updatePassword(updateRequest);
		return new ResponseEntity<>(res, HttpStatus.OK);

	}

	@PutMapping("/update-phone")
	public ResponseEntity<String> updatePhone(@RequestBody UpdateRequest updateRequest) {
		String res = customerService.updatePhone(updateRequest);
		return new ResponseEntity<>(res, HttpStatus.OK);

	}

	@DeleteMapping("/delete")
	public ResponseEntity<String> deleteCustomer(@RequestBody UpdateRequest updateRequest) {
		String res = customerService.deleteCustomer(updateRequest);
		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	@GetMapping("/getCustomerById/{customerId}")
	public ResponseEntity<Customer> getCustomerById(@PathVariable("customerId") Long customerId) {
		Customer res = customerService.getCustomerById(customerId);
		return new ResponseEntity<Customer>(res, HttpStatus.OK);
	}

	@GetMapping("/getToBeDeletedAccounts")
	public ResponseEntity<List<Customer>> getAllCustomer() {
		List<Customer> res = customerService.getToBeDeletedCustomers();
		return new ResponseEntity<List<Customer>>(res, HttpStatus.OK);
	}

}
