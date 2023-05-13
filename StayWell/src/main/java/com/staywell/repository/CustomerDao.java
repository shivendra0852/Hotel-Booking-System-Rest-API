package com.staywell.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.staywell.model.Customer;

public interface CustomerDao extends JpaRepository<Customer, Integer>{

	Optional<Customer> findByEmail(String email);

}
