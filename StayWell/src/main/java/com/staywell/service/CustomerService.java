package com.staywell.service;

import java.util.List;

import com.staywell.dto.request.CustomerRequest;
import com.staywell.dto.request.UpdatePasswordRequest;
import com.staywell.dto.request.UpdateRequest;
import com.staywell.dto.response.CustomerResponse;

public interface CustomerService {

	public CustomerResponse registerCustomer(CustomerRequest customerRequest);

	public String updateName(UpdateRequest updateRequest);

	public String updatePassword(UpdatePasswordRequest updatePasswordRequest);

	public String updatePhone(UpdateRequest updateRequest);

	public String deleteCustomer(UpdateRequest updateRequest);

	public List<CustomerResponse> getToBeDeletedCustomers();

	public CustomerResponse viewProfile();

}
