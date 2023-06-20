package com.staywell.service;

import java.util.List;

import com.staywell.dto.request.CustomerRequest;
import com.staywell.dto.request.UpdateRequest;
import com.staywell.model.Customer;

public interface CustomerService {

	public Customer registerCustomer(CustomerRequest customerRequest);

	public String updateName(UpdateRequest updateRequest);

	public String updatePassword(UpdateRequest updateRequest);

	public String updatePhone(UpdateRequest updateRequest);

	public String deleteCustomer(UpdateRequest updateRequest);

	public List<Customer> getToBeDeletedCustomers();

	public Customer getCustomerById(Long customerId);

}
