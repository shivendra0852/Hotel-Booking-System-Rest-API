package com.staywell.service;

import java.util.List;

import com.staywell.dto.request.ReservationRequest;
import com.staywell.dto.response.ReservationResponse;

public interface ReservationService {

	public ReservationResponse createReservation(Long roomId, ReservationRequest reservationRequest);

	public String cancelReservation(Long reservationId);

	public List<ReservationResponse> getAllReservationsOfHotel();

	public List<ReservationResponse> getAllReservationsOfCustomer();

	public ReservationResponse getReservationById(Long ReservationId);

}
