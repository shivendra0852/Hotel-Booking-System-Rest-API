package com.staywell.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.staywell.model.Customer;

public interface CustomerDao extends JpaRepository<Customer, Long> {

	Optional<Customer> findByEmail(String email);

	List<Customer> findByToBeDeleted(Boolean toBeDeleted);

	@Modifying
	@Query("update Customer set name=?2 where customerId=?1")
	Integer setCustomerEmail(Long customerId, String name);

	@Modifying
	@Query("update Customer set password=?2 where customerId=?1")
	Integer setCustomerPassword(Long customerId, String password);

	@Modifying
	@Query("update Customer set phone=?2 where customerId=?1")
	Integer setCustomerPhone(Long customerId, String phone);

}
