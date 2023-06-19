package com.staywell.service;

import java.util.List;

import com.staywell.dto.CustomerDTO;
import com.staywell.dto.UpdateDetailsDTO;
import com.staywell.model.Customer;

public interface CustomerService {

	public Customer registerCustomer(CustomerDTO customerDTO);

	public String updateName(UpdateDetailsDTO updateRequest);

	public String updatePassword(UpdateDetailsDTO updateRequest);

	public String updatePhone(UpdateDetailsDTO updateRequest);

	public String deleteCustomer(UpdateDetailsDTO updateRequest);

	public List<Customer> getToBeDeletedCustomers();

	public Customer getCustomerById(Long customerId);

}
