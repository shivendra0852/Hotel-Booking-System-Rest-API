package com.staywell.service;

import com.staywell.dto.CustomerDTO;
import com.staywell.exception.CustomerException;
import com.staywell.model.Customer;
import org.hibernate.collection.internal.CustomCollectionTypeSemantics;

import java.util.List;

public interface CustomerService {

        public Customer createCustomer(Customer customer ) throws CustomerException;

        public Customer updateCustomer(CustomerDTO customerDto) throws CustomerException;

        public Customer deleteCustomer() throws CustomerException;

        public List<Customer> getAllCustomer() throws CustomerException;

        public Customer getCustomerById(Integer id) throws CustomerException;




}
