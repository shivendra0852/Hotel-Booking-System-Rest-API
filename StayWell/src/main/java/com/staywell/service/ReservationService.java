package com.staywell.service;

import java.util.List;

import com.staywell.dto.request.ReservationRequest;
import com.staywell.model.Reservation;

public interface ReservationService {

	public Reservation createReservation(Long roomId, ReservationRequest reservationRequest, String paymentType, String txnId);

	public String cancelReservation(Long reservationId);

	public List<Reservation> getAllReservationsOfHotel();

	public List<Reservation> getAllReservationsOfCustomer();

	public Reservation getReservationById(Long ReservationId);

}
