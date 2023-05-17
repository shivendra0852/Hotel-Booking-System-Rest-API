package com.staywell.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.staywell.model.Customer;
import com.staywell.model.Hotel;
import com.staywell.model.Reservation;

public interface ReservationDao extends JpaRepository<Reservation, Integer> {

	List<Reservation> findByCustomer(Customer customer);

//	@Modifying
//	@Query("UPDATE Reservation r SET r.status = 'CLOSED' WHERE r.hotel = ?1 AND r.checkoutDate < CURRENT_DATE")
//	void updateReservationStatus(Hotel hotel);

	@Query("SELECT r FROM Reservation r WHERE r.hotel = ?1 AND r.checkoutDate >= CURRENT_DATE")
	List<Reservation> getAllPendingReservations(Hotel hotel);
}
