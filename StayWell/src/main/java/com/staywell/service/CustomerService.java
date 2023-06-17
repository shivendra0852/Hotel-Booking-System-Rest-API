package com.staywell.service;

import java.util.List;

import com.staywell.dto.CustomerDTO;
import com.staywell.dto.UpdateDetailsDTO;
import com.staywell.exception.CustomerException;
import com.staywell.model.Customer;

public interface CustomerService {

	public Customer registerCustomer(CustomerDTO customerDTO) throws CustomerException;

	public String updateName(UpdateDetailsDTO updateRequest);

	public String updatePassword(UpdateDetailsDTO updateRequest);

	public String updatePhone(UpdateDetailsDTO updateRequest);

	public String deleteCustomer() throws CustomerException;

	public List<Customer> getToBeDeletedCustomers() throws CustomerException;

	public Customer getCustomerById(Long customerId) throws CustomerException;

}
