package com.staywell.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.staywell.dto.ReservationDTO;
import com.staywell.exception.ReservationException;
import com.staywell.model.Customer;
import com.staywell.model.Hotel;
import com.staywell.model.Reservation;
import com.staywell.repository.CustomerDao;
import com.staywell.repository.HotelDao;
import com.staywell.repository.ReservationDao;

@Service
public class ReservationServiceImpl implements ReservationService {

	@Autowired
	private ReservationDao reservationDao;
	
	@Autowired
	private CustomerDao customerDao;
	
	@Autowired
	private HotelDao hotelDao;
	
	@Override
	public Reservation createReservation(Reservation reservation) throws ReservationException {
		
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		Customer customer = customerDao.findByEmail(email).get();
		
		if(customer!=null) {
			reservation.setCustomer(customer);
			if(reservation.getPayment().getPaymentStatus()) {
				return reservationDao.save(reservation);
			} else {
				throw new ReservationException("Payment not successful! please have a payment first.");
			}
		} else {
			throw new ReservationException("Login first.");
		}
		
	}

	@Override
	public Reservation updateReservation(ReservationDTO reservationDto, Integer reservationId) throws ReservationException {
		
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		Customer customer = customerDao.findByEmail(email).get();
		Reservation existingReservation = reservationDao.findById(reservationId).get();
		
		
		if(existingReservation!=null) {
			
			throw new ReservationException("Reservation not found with reservation id: "+reservationId);
			
		}
		
			if(reservationDto.getCheckinDate()!=null) {
				existingReservation.setCheckinDate(reservationDto.getCheckinDate());
			}
			
			if(reservationDto.getCheckoutDate()!=null) {
				existingReservation.setCheckoutDate(reservationDto.getCheckoutDate());
			}
			
			if(reservationDto.getNoOfPerson()!=null) {
				if(reservationDto.getNoOfPerson()<=existingReservation.getRoom().getNoOfPerson()) {
					existingReservation.setNoOfPerson(reservationDto.getNoOfPerson());
				} else {
					throw new ReservationException("No of person cannot fit in the room please book another room!");
				}
			}
			
			return reservationDao.save(existingReservation);
		
	}

	@Override
	public String deleteReservation(Integer reservationId) throws ReservationException {
		
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		Customer customer = customerDao.findByEmail(email).get();
		Reservation existingReservation = reservationDao.findById(reservationId).get();
		
		if(existingReservation==null) {
			
			
			throw new ReservationException("Reservation not found with reservation id: "+reservationId);
			
		}
		
		if(customer==null) {
			throw new ReservationException("Reservation not found with reservation id: "+reservationId);
		}
		
		reservationDao.delete(existingReservation);
		
		return "Reservation deleted successfully.";
		
	}

	@Override
	public List<Reservation> getAllReservations() throws ReservationException {
		
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		Hotel hotel = hotelDao.findByHotelEmail(email).get();
		
			List<Reservation> reservations = reservationDao.findAll();
			List<Reservation> result = new ArrayList<>();
			
			if(reservations.isEmpty()) {
				throw new ReservationException("No reservations found.");
			}
			
			for(Reservation r:reservations) {
				if(r.getRoom().getHotel().getHotelId()==hotel.getHotelId()) {
					result.add(r);
				}
			}
			
			if(result.isEmpty()) {
				throw new ReservationException("No reservations found.");
			}
			
			return result;
		
	}

	@Override
	public Reservation getReservationById(Integer ReservationId) throws ReservationException {
		
		Optional<Reservation> reservation = reservationDao.findById(ReservationId);
		
		if(!reservation.isPresent()) {
			throw new ReservationException("Reservation not found with reservation id: "+ReservationId);
		}
		return reservation.get();
	}

	@Override
	public List<Reservation> getReservationByCustomerId(Integer CustomerId) throws ReservationException {

		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		Customer customer = customerDao.findById(CustomerId).get();
		
		if(customer==null) {
			throw new ReservationException("Customer not found with customerid: "+CustomerId);
		}
		
		List<Reservation> reservations = reservationDao.findByCustomer(customer);
		
		if(!reservations.isEmpty()) {
			return reservations;
		}
		throw new ReservationException("No reservations found with customerid: "+CustomerId);

	}

}
