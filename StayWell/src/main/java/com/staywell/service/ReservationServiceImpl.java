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
			
			if(customer!=null) {
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
			} else {
				throw new ReservationException("Reservation not found with reservation id: "+reservationId);
			}
			
		} else {
			throw new ReservationException("Reservation not found with reservation id: "+reservationId);
		}
		
	}

	@Override
	public String deleteReservation(Integer reservationId) throws ReservationException {
		
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		Customer customer = customerDao.findByEmail(email).get();
		Reservation existingReservation = reservationDao.findById(reservationId).get();
		
		if(existingReservation!=null) {
			
			if(customer!=null) {
				reservationDao.delete(existingReservation);
				return "Reservation deleted successfully.";
			} else {	
				throw new ReservationException("Reservation not found with reservation id: "+reservationId);
			}
			
		} else {
			throw new ReservationException("Reservation not found with reservation id: "+reservationId);
		}
		
	}

	@Override
	public List<Reservation> getAllReservations() throws ReservationException {
		
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		Hotel hotel = hotelDao.findByHotelEmail(email).get();
		
		if(hotel!=null) {
			
			List<Reservation> reservations = reservationDao.findAll();
			List<Reservation> result = new ArrayList<>();
			
			if(reservations.isEmpty()) {
				throw new ReservationException("No reservations found.");
			} else {
				for(Reservation r:reservations) {
					if(r.getRoom().getHotel().getId()==hotel.getId()) {
						result.add(r);
					}
				}
				
				if(result.isEmpty()) {
					throw new ReservationException("No reservations found.");
				} else {
					return result;
				}
			}
			
		} else {
			throw new ReservationException("Please login first");
		}
		
	}

	@Override
	public Reservation getReservationById(Integer ReservationId) throws ReservationException {
		
		Optional<Reservation> reservation = reservationDao.findById(ReservationId);
		
		if(reservation.isPresent()) {
			return reservation.get();
		} else {
			throw new ReservationException("Reservation not found with reservation id: "+ReservationId);
		}
	}

	@Override
	public List<Reservation> getReservationByCustomerId(Integer CustomerId) throws ReservationException {

		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		Customer customer = customerDao.findByEmail(email).get();
		
		if(customer!=null) {
			
			List<Reservation> reservations = reservationDao.findByCustomer(customer);
			
			if(reservations.isEmpty()) {
				throw new ReservationException("No reservations found with customerid: "+CustomerId);
			} else {
				return reservations;
			}
			
		} else {
			throw new ReservationException("Customer not found with customerid: "+CustomerId);
		}

	}

}
