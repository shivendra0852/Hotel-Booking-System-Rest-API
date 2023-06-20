package com.staywell.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.staywell.dto.request.ReservationRequest;
import com.staywell.dto.response.ReservationResponse;
import com.staywell.enums.ReservationStatus;
import com.staywell.exception.ReservationException;
import com.staywell.exception.RoomException;
import com.staywell.model.Customer;
import com.staywell.model.Hotel;
import com.staywell.model.Reservation;
import com.staywell.model.Room;
import com.staywell.repository.CustomerDao;
import com.staywell.repository.HotelDao;
import com.staywell.repository.ReservationDao;
import com.staywell.repository.RoomDao;
import com.staywell.service.ReservationService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
@Transactional
public class ReservationServiceImpl implements ReservationService {

	private ReservationDao reservationDao;
	private CustomerDao customerDao;
	private HotelDao hotelDao;
	private RoomDao roomDao;

	@Override
	public ReservationResponse createReservation(Long roomId, ReservationRequest reservationRequest) {
		Customer customer = getCurrentLoggedInCustomer();

		Optional<Room> opt = roomDao.findById(roomId);
		if (opt.isEmpty())
			throw new RoomException("Room Not Found With Id : " + roomId);
		Room room = opt.get();

		Hotel hotel = room.getHotel();
		reservationDao.updateReservationStatus(hotel);

		List<Reservation> reservations = reservationDao.findByRoomAndStatus(room, ReservationStatus.BOOKED);

		LocalDate checkIn = reservationRequest.getCheckinDate();
		LocalDate checkOut = reservationRequest.getCheckinDate();
		log.info("Checking Room availability for dates : " + checkIn + " -> " + checkOut);
		for (Reservation r : reservations) {
			if ((checkIn.isEqual(r.getCheckinDate()) || checkIn.isEqual(r.getCheckinDate()))
					|| (checkOut.isEqual(r.getCheckinDate()) || checkOut.isEqual(r.getCheckinDate()))
					|| (checkIn.isAfter(r.getCheckinDate()) && checkIn.isBefore(r.getCheckinDate()))
					|| (checkOut.isAfter(r.getCheckinDate()) && checkOut.isBefore(r.getCheckinDate()))) {
				throw new ReservationException("Room not available for this date! Your payment will be transferred back to your account within 30 minutes");
			}
		}

		log.info("Building Reservation");
		Reservation reservation = buildReservation(reservationRequest);

		log.info("Assigning Reservation to the Room : " + roomId);
		room.getReservations().add(reservation);
		reservation.setRoom(room);

		log.info("Assigning Reservation to the Hotel : " + hotel.getName());
		hotel.getReservations().add(reservation);
		reservation.setHotel(hotel);

		log.info("Assigning Reservation to the Customer : " + customer.getName());
		customer.getReservations().add(reservation);
		reservation.setCustomer(customer);

		reservationDao.save(reservation);

		log.info("Reservation successfull");
		return buildReservationResponse(reservation);
	}

	@Override
	public String cancelReservation(Long reservationId) {
		Customer customer = getCurrentLoggedInCustomer();

		Optional<Reservation> opt = reservationDao.findById(reservationId);
		if (opt.isEmpty())
			throw new ReservationException("Reservation not found with reservation id : " + reservationId);
		Reservation reservation = opt.get();

		log.info("Checking if cancellation is allowed");
		if (reservation.getCheckinDate().isEqual(LocalDate.now())
				|| reservation.getCheckinDate().isBefore(LocalDate.now()))
			throw new ReservationException("Reservation can't be cancelled now!");

		log.info("Removing reference of Reservation from Room");
		Room room = reservation.getRoom();
		List<Reservation> curReservationsOfRoom = room.getReservations();
		curReservationsOfRoom.remove(reservation);
		room.setReservations(curReservationsOfRoom);

		log.info("Removing reference of Reservation from Hotel");
		Hotel hotel = reservation.getHotel();
		List<Reservation> curReservationsOfHotel = hotel.getReservations();
		curReservationsOfHotel.remove(reservation);
		hotel.setReservations(curReservationsOfHotel);

		log.info("Removing reference of Reservation from Customer");
		List<Reservation> curReservationsOfCustomer = customer.getReservations();
		curReservationsOfCustomer.remove(reservation);
		customer.setReservations(curReservationsOfCustomer);

		log.info("Deletion in progress");
		reservationDao.delete(reservation);

		log.info("Reservation cancelled successfully");
		return "Reservation cancelled successfully";
	}

	@Override
	public List<ReservationResponse> getAllReservationsOfHotel() {
		Hotel hotel = getCurrentLoggedInHotel();
		List<Reservation> reservations = reservationDao.findByHotel(hotel);
		if (reservations.isEmpty())
			throw new ReservationException("Reservations Not Found!");
		return reservations.stream().map(this::buildReservationResponse).collect(Collectors.toList());
	}

	@Override
	public List<ReservationResponse> getAllReservationsOfCustomer() {
		Customer customer = getCurrentLoggedInCustomer();
		List<Reservation> reservations = reservationDao.findByCustomer(customer);
		if (reservations.isEmpty())
			throw new ReservationException("Reservations Not Found!");
		return reservations.stream().map(this::buildReservationResponse).collect(Collectors.toList());
	}

	@Override
	public ReservationResponse getReservationById(Long ReservationId) {
		Optional<Reservation> optional = reservationDao.findById(ReservationId);
		if (optional.isEmpty())
			throw new ReservationException("Reservation not found with reservation id: " + ReservationId);
		return buildReservationResponse(optional.get());
	}

	private Hotel getCurrentLoggedInHotel() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		return hotelDao.findByHotelEmail(email).get();
	}

	private Customer getCurrentLoggedInCustomer() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		return customerDao.findByEmail(email).get();
	}

	private Reservation buildReservation(ReservationRequest reservationRequest) {
		return Reservation.builder()
				.checkinDate(reservationRequest.getCheckinDate())
				.checkoutDate(reservationRequest.getCheckoutDate())
				.noOfPerson(reservationRequest.getNoOfPerson())
				.status(ReservationStatus.BOOKED)
				.payment(reservationRequest.getPayment())
				.build();
	}

	private ReservationResponse buildReservationResponse(Reservation reservation) {
		return ReservationResponse.builder()
				.reservationId(reservation.getReservationId())
				.customerName(reservation.getCustomer().getName())
				.roomNumber(reservation.getRoom().getRoomNumber())
				.hotelName(reservation.getHotel().getName())
				.checkinDate(reservation.getCheckinDate())
				.checkoutDate(reservation.getCheckoutDate())
				.noOfPerson(reservation.getNoOfPerson())
				.payment(reservation.getPayment())
				.status(reservation.getStatus())
				.build();
	}

}
