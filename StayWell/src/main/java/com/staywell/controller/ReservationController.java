package com.staywell.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.staywell.dto.request.ReservationRequest;
import com.staywell.dto.response.ReservationResponse;
import com.staywell.exception.ReservationException;
import com.staywell.service.ReservationService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/staywell/reservations")
@AllArgsConstructor
public class ReservationController {

	private ReservationService reservationService;

	@PostMapping("/book/{roomId}")
	public ResponseEntity<ReservationResponse> createReservation(@PathVariable("roomId") Long roomId,
			@RequestBody ReservationRequest reservationRequest) {
		return new ResponseEntity<>(reservationService.createReservation(roomId, reservationRequest),
				HttpStatus.CREATED);
	}

	@PutMapping("/cancel/{reservationId}")
	public ResponseEntity<String> cancelReservation(@PathVariable("reservationId") Long reservationId)
			throws ReservationException {
		return new ResponseEntity<>(reservationService.cancelReservation(reservationId), HttpStatus.ACCEPTED);
	}

	@GetMapping("/get/by-hotel")
	public ResponseEntity<List<ReservationResponse>> getAllReservationsOfHotel() {
		return new ResponseEntity<>(reservationService.getAllReservationsOfHotel(), HttpStatus.OK);
	}

	@GetMapping("/get/by-customer")
	public ResponseEntity<List<ReservationResponse>> getAllReservationsOfCustomer() {
		return new ResponseEntity<>(reservationService.getAllReservationsOfCustomer(), HttpStatus.OK);
	}

	@GetMapping("/get/by-id/{reservationId}")
	public ResponseEntity<ReservationResponse> getReservationById(@PathVariable("reservationId") Long reservationId) {
		return new ResponseEntity<>(reservationService.getReservationById(reservationId), HttpStatus.OK);
	}

}
