package com.staywell.service;

import java.util.List;

import com.staywell.dto.ReservationDTO;
import com.staywell.exception.ReservationException;
import com.staywell.model.Reservation;

public interface ReservationService {

	public Reservation createReservation(Reservation reservation) throws ReservationException;
	
	public Reservation updateReservation(ReservationDTO reservationDto, Integer reservationId) throws ReservationException;
	
	public String deleteReservation(Integer reservationId) throws ReservationException;
	
	public List<Reservation> getAllReservations() throws ReservationException;
	
	public Reservation getReservationById(Integer ReservationId) throws ReservationException;
	
	public List<Reservation> getReservationByCustomerId(Integer CustomerId) throws ReservationException;

	
}
