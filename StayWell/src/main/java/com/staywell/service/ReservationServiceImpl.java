package com.staywell.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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

@Service
public class ReservationServiceImpl implements ReservationService {

	@Autowired
	private ReservationDao reservationDao;

	@Autowired
	private CustomerDao customerDao;

	@Autowired
	private HotelDao hotelDao;

	@Autowired
	private RoomDao roomDao;

//	@Override
//	public Reservation createReservation(Integer roomId, Reservation reservation, String paymentType)
//			throws ReservationException, RoomException {
//
//		Customer customer = getCurrentLoggedInCustomer();
//
//		Optional<Room> opt = roomDao.findById(roomId);
//		if (opt.isEmpty())
//			throw new RoomException("Room Not Found With Id : " + roomId);
//		Room room = opt.get();
//
//		Hotel hotel = room.getHotel();
//		reservationDao.updateReservationStatus(hotel);
//
//		List<Reservation> reservations = reservationDao.findByRoomAndStatus(room, ReservationStatus.BOOKED);
//
//		LocalDate checkIn = reservation.getCheckinDate();
//		LocalDate checkOut = reservation.getCheckinDate();
//		for (Reservation r : reservations) {
//			if ((checkIn.isEqual(r.getCheckinDate()) || checkIn.isEqual(r.getCheckinDate()))
//					|| (checkOut.isEqual(r.getCheckinDate()) || checkOut.isEqual(r.getCheckinDate()))
//					|| (checkIn.isAfter(r.getCheckinDate()) && checkIn.isBefore(r.getCheckinDate()))
//					|| (checkOut.isAfter(r.getCheckinDate()) && checkOut.isBefore(r.getCheckinDate()))) {
//				throw new ReservationException("Room Not Available for this date!");
//			}
//		}
//
//		room.getReservations().add(reservation);
//		hotel.getReservations().add(reservation);
//		customer.getReservations().add(reservation);
//
//		reservation.setRoom(room);
//		reservation.setHotel(hotel);
//		reservation.setCustomer(customer);
//
//		reservation.setStatus(ReservationStatus.BOOKED);
//		reservation.setPayment(new Payment(PaymentType.valueOf(paymentType), true));
//
//		return reservationDao.save(reservation);
//
//	}

	@Override
	public String cancelReservation(Integer reservationId) throws ReservationException {

		Customer customer = getCurrentLoggedInCustomer();

		Reservation reservation = null;

		List<Reservation> reservations = customer.getReservations();
		for (Reservation r : reservations) {
			if (r.getReservationId() == reservationId) {
				reservation = r;
			}
		}

		if (reservation == null)
			throw new ReservationException("Reservation not found with reservation id : " + reservationId);

		if (reservation.getCheckinDate().isEqual(LocalDate.now())
				|| reservation.getCheckinDate().isBefore(LocalDate.now()))
			throw new ReservationException("Reservation can't be cancelled now!");

		Room room = reservation.getRoom();
		List<Reservation> curReservationsOfRoom = room.getReservations();
		curReservationsOfRoom.remove(reservation);
		room.setReservations(curReservationsOfRoom);

		Hotel hotel = reservation.getHotel();
		List<Reservation> curReservationsOfHotel = hotel.getReservations();
		curReservationsOfHotel.remove(reservation);
		hotel.setReservations(curReservationsOfHotel);

		List<Reservation> curReservationsOfCustomer = customer.getReservations();
		curReservationsOfCustomer.remove(reservation);
		customer.setReservations(curReservationsOfCustomer);

		roomDao.save(room);
		hotelDao.save(hotel);
		customerDao.save(customer);

		reservationDao.delete(reservation);

		return "Reservation cancelled successfully.";

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

	@Override
	public Reservation createReservation(Integer roomId, Reservation reservation, String paymentType)
			throws ReservationException, RoomException {
		// TODO Auto-generated method stub
		return null;
	}

}
