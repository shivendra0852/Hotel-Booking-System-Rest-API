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

import com.staywell.dto.ReservationDTO;
import com.staywell.exception.ReservationException;
import com.staywell.exception.RoomException;
import com.staywell.model.Reservation;
import com.staywell.service.ReservationService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/staywell/reservations")
@AllArgsConstructor
public class ReservationController {

	private ReservationService reservationService;

	@PostMapping("/book/{roomId}/{paymentType}/{txnId}")
	public ResponseEntity<Reservation> createReservation(@PathVariable("roomId") Long roomId,
			@PathVariable("paymentType") String paymentType, @PathVariable("txnId") String txnId,
			@RequestBody ReservationDTO reservationDTO) throws ReservationException, RoomException {
		return new ResponseEntity<>(reservationService.createReservation(roomId, reservationDTO, paymentType, txnId),
				HttpStatus.CREATED);
	}

	@PutMapping("/cancel/{reservationId}")
	public ResponseEntity<String> cancelReservation(@PathVariable("reservationId") Long reservationId)
			throws ReservationException {
		return new ResponseEntity<>(reservationService.cancelReservation(reservationId), HttpStatus.ACCEPTED);
	}

	@GetMapping("/get-by-hotel")
	public ResponseEntity<List<Reservation>> getAllReservationsOfHotel() throws ReservationException {
		return new ResponseEntity<>(reservationService.getAllReservationsOfHotel(), HttpStatus.OK);
	}

	@GetMapping("/get-by-customer")
	public ResponseEntity<List<Reservation>> getAllReservationsOfCustomer() throws ReservationException {
		return new ResponseEntity<>(reservationService.getAllReservationsOfCustomer(), HttpStatus.OK);
	}

	@GetMapping("/get-by-id/{reservationId}")
	public ResponseEntity<Reservation> getReservationById(@PathVariable("reservationId") Long reservationId)
			throws ReservationException {
		return new ResponseEntity<>(reservationService.getReservationById(reservationId), HttpStatus.OK);
	}

}
