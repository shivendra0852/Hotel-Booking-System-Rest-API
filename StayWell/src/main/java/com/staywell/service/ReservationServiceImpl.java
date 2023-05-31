package com.staywell.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.staywell.dto.ReservationDTO;
import com.staywell.enums.PaymentType;
import com.staywell.enums.ReservationStatus;
import com.staywell.exception.ReservationException;
import com.staywell.exception.RoomException;
import com.staywell.model.Customer;
import com.staywell.model.Hotel;
import com.staywell.model.Payment;
import com.staywell.model.Reservation;
import com.staywell.model.Room;
import com.staywell.repository.CustomerDao;
import com.staywell.repository.HotelDao;
import com.staywell.repository.ReservationDao;
import com.staywell.repository.RoomDao;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Service
@Slf4j
public class ReservationServiceImpl implements ReservationService {

	private ReservationDao reservationDao;
	private CustomerDao customerDao;
	private HotelDao hotelDao;
	private RoomDao roomDao;

	@Override
	public Reservation createReservation(Integer roomId, ReservationDTO reservationDTO, String paymentType, String txnId)
			throws ReservationException, RoomException {

		Customer customer = getCurrentLoggedInCustomer();

		Optional<Room> opt = roomDao.findById(roomId);
		if (opt.isEmpty())
			throw new RoomException("Room Not Found With Id : " + roomId);
		Room room = opt.get();

		Hotel hotel = room.getHotel();
		reservationDao.updateReservationStatus(hotel);

		List<Reservation> reservations = reservationDao.findByRoomAndStatus(room, ReservationStatus.BOOKED);

		LocalDate checkIn = reservationDTO.getCheckinDate();
		LocalDate checkOut = reservationDTO.getCheckinDate();
		log.info("Checking if Room is available for : " + checkIn + " -> " + checkOut);
		for (Reservation r : reservations) {
			if ((checkIn.isEqual(r.getCheckinDate()) || checkIn.isEqual(r.getCheckinDate()))
					|| (checkOut.isEqual(r.getCheckinDate()) || checkOut.isEqual(r.getCheckinDate()))
					|| (checkIn.isAfter(r.getCheckinDate()) && checkIn.isBefore(r.getCheckinDate()))
					|| (checkOut.isAfter(r.getCheckinDate()) && checkOut.isBefore(r.getCheckinDate()))) {
				throw new ReservationException("Room Not Available for this date!");
			}
		}

		Reservation reservation = buildReservation(reservationDTO);
		reservation.setStatus(ReservationStatus.BOOKED);
		reservation.setPayment(new Payment(PaymentType.valueOf(paymentType), txnId));
		reservationDao.save(reservation);

		log.info("Assigning Reservation to the Room : " + roomId);
		room.getReservations().add(reservation);
		reservation.setRoom(room);
		roomDao.save(room);

		log.info("Assigning Reservation to the Hotel : " + hotel.getName());
		hotel.getReservations().add(reservation);
		reservation.setHotel(hotel);
		hotelDao.save(hotel);

		log.info("Assigning Reservation to the Customer : " + customer.getName());
		customer.getReservations().add(reservation);
		reservation.setCustomer(customer);
		customerDao.save(customer);

		log.info("Reservation success");
		return reservation;

	}

	@Override
	public String cancelReservation(Integer reservationId) throws ReservationException {

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
		roomDao.save(room);
		
		log.info("Removing reference of Reservation from Hotel");
		Hotel hotel = reservation.getHotel();
		List<Reservation> curReservationsOfHotel = hotel.getReservations();
		curReservationsOfHotel.remove(reservation);
		hotel.setReservations(curReservationsOfHotel);
		hotelDao.save(hotel);
		
		log.info("Removing reference of Reservation from Customer");
		List<Reservation> curReservationsOfCustomer = customer.getReservations();
		curReservationsOfCustomer.remove(reservation);
		customer.setReservations(curReservationsOfCustomer);
		customerDao.save(customer);
		
		reservationDao.delete(reservation);

		log.info("Reservation cancelled successfully.");
		return "Reservation cancelled successfully";

	}

	@Override
	public List<Reservation> getAllReservationsOfHotel() throws ReservationException {

		Hotel hotel = getCurrentLoggedInHotel();

		List<Reservation> reservations = reservationDao.findByHotel(hotel);

		if (reservations.isEmpty())
			throw new ReservationException("Reservations Not Found!");

		return reservations;

	}

	@Override
	public List<Reservation> getAllReservationsOfCustomer() throws ReservationException {

		Customer customer = getCurrentLoggedInCustomer();

		List<Reservation> reservations = reservationDao.findByCustomer(customer);

		if (reservations.isEmpty())
			throw new ReservationException("Reservations Not Found!");

		return reservations;

	}

	@Override
	public Reservation getReservationById(Integer ReservationId) throws ReservationException {

		Optional<Reservation> reservation = reservationDao.findById(ReservationId);

		if (reservation.isEmpty())
			throw new ReservationException("Reservation not found with reservation id: " + ReservationId);

		return reservation.get();

	}

	private Hotel getCurrentLoggedInHotel() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		return hotelDao.findByHotelEmail(email).get();
	}

	private Customer getCurrentLoggedInCustomer() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		return customerDao.findByEmail(email).get();
	}

	private Reservation buildReservation(ReservationDTO reservationDTO) {
		return Reservation.builder()
				.checkinDate(reservationDTO.getCheckinDate())
				.checkoutDate(reservationDTO.getCheckoutDate())
				.noOfPerson(reservationDTO.getNoOfPerson())
				.build();
	}

}
