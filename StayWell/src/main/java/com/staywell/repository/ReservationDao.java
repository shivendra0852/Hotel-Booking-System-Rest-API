package com.staywell.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.staywell.model.Customer;
import com.staywell.model.Reservation;

public interface ReservationDao extends JpaRepository<Reservation, Integer>{

	List<Reservation> findByCustomer(Customer customer);
}
