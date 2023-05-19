package com.staywell.service;

import java.util.List;

import com.staywell.exception.ReservationException;
import com.staywell.exception.RoomException;
import com.staywell.model.Reservation;

public interface ReservationService {

	public Reservation createReservation(Integer roomId, Reservation reservation, String paymentType) throws ReservationException, RoomException;
	
	public String cancelReservation(Integer reservationId) throws ReservationException;
	
	public List<Reservation> getAllReservationsOfHotel() throws ReservationException;
	
	public List<Reservation> getAllReservationsOfCustomer() throws ReservationException;
	
	public Reservation getReservationById(Integer ReservationId) throws ReservationException;

}
