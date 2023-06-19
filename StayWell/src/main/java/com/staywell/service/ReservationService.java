package com.staywell.service;

import java.util.List;

import com.staywell.dto.ReservationDTO;
import com.staywell.model.Reservation;

public interface ReservationService {

	public Reservation createReservation(Long roomId, ReservationDTO reservationDTO, String paymentType, String txnId);

	public String cancelReservation(Long reservationId);

	public List<Reservation> getAllReservationsOfHotel();

	public List<Reservation> getAllReservationsOfCustomer();

	public Reservation getReservationById(Long ReservationId);

}
