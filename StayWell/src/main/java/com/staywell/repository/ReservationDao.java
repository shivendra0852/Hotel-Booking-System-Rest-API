package com.staywell.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.staywell.enums.ReservationStatus;
import com.staywell.model.Customer;
import com.staywell.model.Hotel;
import com.staywell.model.Reservation;
import com.staywell.model.Room;

public interface ReservationDao extends JpaRepository<Reservation, Long> {

	List<Reservation> findByCustomer(Customer customer);

	@Modifying
	@Query("UPDATE Reservation r SET r.status = com.staywell.enums.ReservationStatus.CLOSED WHERE r.hotel = ?1 AND r.checkoutDate < CURDATE()")
	void updateReservationStatus(Hotel hotel);

	@Query("SELECT r FROM Reservation r WHERE r.hotel = ?1 AND r.checkoutDate >= CURDATE()")
	List<Reservation> getPendingReservationsOfHotel(Hotel hotel);

	List<Reservation> findByRoomAndStatus(Room room, ReservationStatus status);

	List<Reservation> findByHotel(Hotel hotel);

	List<Reservation> findByHotelAndCustomer(Hotel hotel, Customer customer);

}
