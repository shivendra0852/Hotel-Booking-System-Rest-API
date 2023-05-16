package com.staywell.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.staywell.model.Customer;
import com.staywell.model.Hotel;
import com.staywell.model.Reservation;

public interface ReservationDao extends JpaRepository<Reservation, Integer>{

	
	
	List<Reservation> findByCustomer(Customer customer);
	
	
	@Query("update Reservation r set r.status = 'CLOSED' where r.hotel = ?1 and r.checkoutDate < cur_date()")
	void updateReservationStatus(Hotel hotel);
	
	@Query("select r from Reservation r where r.hotel = ?1 and r.checkoutDate >= cur_date()")
	List<Reservation> getAllPendingReservations(Hotel hotel);
	
	
}
