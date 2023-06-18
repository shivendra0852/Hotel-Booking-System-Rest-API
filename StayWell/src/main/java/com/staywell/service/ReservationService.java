package com.staywell.service;

import java.util.List;

import com.staywell.dto.ReservationDTO;
import com.staywell.exception.ReservationException;
import com.staywell.exception.RoomException;
import com.staywell.model.Reservation;

public interface ReservationService {

	public Reservation createReservation(Long roomId, ReservationDTO reservationDTO, String paymentType, String txnId) throws ReservationException, RoomException;
	
	public String cancelReservation(Long reservationId) throws ReservationException;
	
	public List<Reservation> getAllReservationsOfHotel() throws ReservationException;
	
	public List<Reservation> getAllReservationsOfCustomer() throws ReservationException;
	
	public Reservation getReservationById(Long ReservationId) throws ReservationException;

}
