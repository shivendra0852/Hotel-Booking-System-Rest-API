package com.staywell.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.staywell.dto.HotelDTO;
import com.staywell.dto.UpdateHotelDetailsDTO;
import com.staywell.enums.Role;
import com.staywell.exception.HotelException;
import com.staywell.model.Address;
import com.staywell.model.Customer;
import com.staywell.model.Hotel;
import com.staywell.model.Reservation;
import com.staywell.repository.CustomerDao;
import com.staywell.repository.HotelDao;
import com.staywell.repository.ReservationDao;

@Service
public class HotelServiceImpl implements HotelService {

	@Autowired
	private HotelDao hotelDao;

	@Autowired
	private CustomerDao customerDao;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private ReservationDao reservationDao;

	@Override
	public Hotel registerHotel(HotelDTO hotelRequest) {
		/* Checking if there exists a user or a hotel with the provided email */
		if (isEmailExists(hotelRequest.getEmail())) {
			throw new HotelException("This email is already registered. Please use a different email to register.");
		}

		/* Creating a hotel object and mapping attributes from request DTO to hotel entity */
		Hotel hotel = new Hotel();
		hotel.setName(hotelRequest.getName());
		hotel.setHotelEmail(hotelRequest.getEmail());
		hotel.setHotelPhone(hotelRequest.getPhone());
		hotel.setHotelTelephone(hotelRequest.getTelephone());
		hotel.setPassword(passwordEncoder.encode(hotelRequest.getPassword()));
		hotel.setHotelType(hotelRequest.getType());
		hotel.setRole(Role.HOTEL.toString());
		hotel.setAddress(hotelRequest.getAddress());

		/* Saving to the database */
		return hotelDao.save(hotel);
	}

	@Override
	public Hotel getHotelById(Long id) {
		Optional<Hotel> existence = hotelDao.findById(id);
		if (existence.isPresent()) {
			return existence.get();
		}
		throw new HotelException("No hotel found with the id " + id);
	}

	@Override
	public boolean deactivateHotelAccount() {
		Hotel currentHotel = getCurrentLoggedInHotel();
		reservationDao.updateReservationStatus(currentHotel);
		List<Reservation> reservations = reservationDao.getAllPendingReservations(currentHotel);
		if (reservations.isEmpty()) {
			hotelDao.delete(currentHotel);
			return true;
		}
		throw new HotelException("Hotel " + currentHotel.getName()
					+ " has reservations booked for the future. Please serve/cancel those reservations before deleting the account.");
	}

	@Override
	public List<Hotel> getHotelsNearMe() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		Customer customer = customerDao.findByEmail(email).orElseThrow(
				() -> new HotelException("Failed to fetch the customer with the email: " + email));
		List<Hotel> hotels = hotelDao.findByAddress(customer.getAddress());
		if(hotels.isEmpty()) throw new HotelException("Hotels Not Found In Your Area!");
		return hotels;
	}

	@Override
	public List<Hotel> getHotelsInCity(String city) {
		Address address = new Address();
		address.setCity(city);
		List<Hotel> hotels = hotelDao.findByAddress(address);
		if(hotels.isEmpty()) throw new HotelException("Hotels Not Found In Your Area!");
		return hotels;
	}

	@Override
	public Hotel updateEmail(UpdateHotelDetailsDTO updateRequest) {
		Hotel currentHotel = getCurrentLoggedInHotel();
		String password = updateRequest.getPassword();
		if (!passwordEncoder.matches(password, currentHotel.getPassword())) {
			throw new HotelException("Wrong credentials!");
		}
		hotelDao.setEmailOfHotel(currentHotel.getHotelId(), updateRequest.getField());
		return hotelDao.findById(currentHotel.getHotelId()).get();
	}

	@Override
	public Hotel updateName(UpdateHotelDetailsDTO updateRequest) {
		Hotel currentHotel = getCurrentLoggedInHotel();
		String password = updateRequest.getPassword();
		if (!passwordEncoder.matches(password, currentHotel.getPassword())) {
			throw new HotelException("Wrong credentials!");
		}
		 hotelDao.setNameOfHotel(currentHotel.getHotelId(), updateRequest.getField());
		 return hotelDao.findById(currentHotel.getHotelId()).get();
	}

	@Override
	public Hotel updatePhone(UpdateHotelDetailsDTO updateRequest) {
		Hotel currentHotel = getCurrentLoggedInHotel();
		String password = updateRequest.getPassword();
		if (!passwordEncoder.matches(password, currentHotel.getPassword())) {
			throw new HotelException("Wrong credentials!");
		}
		 hotelDao.setPhoneOfHotel(currentHotel.getHotelId(), updateRequest.getField());
		 return hotelDao.findById(currentHotel.getHotelId()).get();
	}

	@Override
	public Hotel updateTelephone(UpdateHotelDetailsDTO updateRequest) {
		Hotel currentHotel = getCurrentLoggedInHotel();
		String password = updateRequest.getPassword();
		if (!passwordEncoder.matches(password, currentHotel.getPassword())) {
			throw new HotelException("Wrong credentials!");
		}
		 hotelDao.setTelephoneOfHotel(currentHotel.getHotelId(), updateRequest.getField());
		 return hotelDao.findById(currentHotel.getHotelId()).get();
	}

	public boolean isEmailExists(String email) {
		return customerDao.findByEmail(email).isPresent() || hotelDao.findByHotelEmail(email).isPresent();
	}

	private Hotel getCurrentLoggedInHotel() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		return hotelDao.findByHotelEmail(email)
				.orElseThrow(() -> new HotelException("Failed to fetch the hotel with the email: " + email));
	}
}
