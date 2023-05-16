package com.staywell.service;

import java.util.List;

import com.staywell.dto.CustomerDTO;
import com.staywell.exception.CustomerException;
import com.staywell.model.Customer;

public interface CustomerService {

        public Customer createCustomer(Customer customer ) throws CustomerException;

        public Customer updateCustomer(CustomerDTO customerDto) throws CustomerException;

        public Customer deleteCustomer() throws CustomerException;

        public List<Customer> getAllCustomer() throws CustomerException;

        public Customer getCustomerById(Integer id) throws CustomerException;




}
