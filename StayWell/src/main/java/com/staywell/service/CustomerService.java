package com.staywell.service;

import java.util.List;

import org.springframework.security.core.Authentication;

import com.staywell.dto.CustomerDTO;
import com.staywell.exception.CustomerException;
import com.staywell.model.Customer;

public interface CustomerService {

        public Customer registerCustomer(CustomerDTO customerDTO) throws CustomerException;

        public Customer updateCustomer(CustomerDTO customerDto) throws CustomerException;

        public Customer deleteCustomer(Authentication authentication) throws CustomerException;

        public List<Customer> getAllCustomer() throws CustomerException;

        public Customer getCustomerById(Long customerId) throws CustomerException;

}
