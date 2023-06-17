package com.staywell.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.staywell.dto.HotelDTO;
import com.staywell.dto.UpdateDetailsDTO;
import com.staywell.enums.Role;
import com.staywell.exception.HotelException;
import com.staywell.model.Address;
import com.staywell.model.Customer;
import com.staywell.model.Hotel;
import com.staywell.model.Reservation;
import com.staywell.repository.CustomerDao;
import com.staywell.repository.HotelDao;
import com.staywell.repository.ReservationDao;
import com.staywell.service.HotelService;

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
	public Hotel registerHotel(HotelDTO hotelDTO) {

		if (isEmailExists(hotelDTO.getHotelEmail())) {
			throw new HotelException("This email is already registered. Please use a different email to register.");
		}

		if (hotelWithNameAlreadyExitsInYourCity(hotelDTO.getName(), hotelDTO.getAddress())) {
			throw new HotelException("Hotel already exits in your city with name : " + hotelDTO.getName());
		}

		Hotel hotel = buildHotel(hotelDTO);

		return hotelDao.save(hotel);
	}

	@Override
	public Hotel getHotelById(Long id) {
		Optional<Hotel> existence = hotelDao.findById(id);
		if (existence.isPresent()) {
			return existence.get();
		}
		throw new HotelException("No hotel found with id " + id);
	}

	@Override
	public boolean deactivateHotelAccount() {
		Hotel currentHotel = getCurrentLoggedInHotel();
		reservationDao.updateReservationStatus(currentHotel);
		List<Reservation> reservations = reservationDao.getPendingReservationsOfHotel(currentHotel);
		if (reservations.isEmpty()) {
			hotelDao.delete(currentHotel);
			return true;
		}
		throw new HotelException("Hotel " + currentHotel.getName()
				+ " has reservations booked for the future. Please serve/cancel those reservations before deleting the account.");
	}

	@Override
	public List<Hotel> getHotelsNearMe() {

		Customer customer = getCurrentLoggedInCustomer();

		List<Hotel> hotels = hotelDao.findByAddress(customer.getAddress());
		if (hotels.isEmpty())
			throw new HotelException("Hotels Not Found In Your Area!");
		return hotels;
	}

	@Override
	public List<Hotel> getHotelsInCity(String city) {
		Address address = new Address();
		address.setCity(city);
		List<Hotel> hotels = hotelDao.findByAddress(address);
		if (hotels.isEmpty())
			throw new HotelException("Hotels Not Found In Your Area!");
		return hotels;
	}

	@Override
	public Hotel updateEmail(UpdateDetailsDTO updateRequest) {
		Hotel currentHotel = getCurrentLoggedInHotel();
		String password = updateRequest.getPassword();
		if (!passwordEncoder.matches(password, currentHotel.getPassword())) {
			throw new HotelException("Wrong credentials!");
		}
		hotelDao.setEmailOfHotel(currentHotel.getHotelId(), updateRequest.getField());
		return hotelDao.findById(currentHotel.getHotelId()).get();
	}

	@Override
	public Hotel updateName(UpdateDetailsDTO updateRequest) {
		Hotel currentHotel = getCurrentLoggedInHotel();
		String password = updateRequest.getPassword();
		if (!passwordEncoder.matches(password, currentHotel.getPassword())) {
			throw new HotelException("Wrong credentials!");
		}
		hotelDao.setNameOfHotel(currentHotel.getHotelId(), updateRequest.getField());
		return hotelDao.findById(currentHotel.getHotelId()).get();
	}

	@Override
	public Hotel updatePhone(UpdateDetailsDTO updateRequest) {
		Hotel currentHotel = getCurrentLoggedInHotel();
		String password = updateRequest.getPassword();
		if (!passwordEncoder.matches(password, currentHotel.getPassword())) {
			throw new HotelException("Wrong credentials!");
		}
		hotelDao.setPhoneOfHotel(currentHotel.getHotelId(), updateRequest.getField());
		return hotelDao.findById(currentHotel.getHotelId()).get();
	}

	@Override
	public Hotel updateTelephone(UpdateDetailsDTO updateRequest) {
		Hotel currentHotel = getCurrentLoggedInHotel();
		String password = updateRequest.getPassword();
		if (!passwordEncoder.matches(password, currentHotel.getPassword())) {
			throw new HotelException("Wrong credentials!");
		}
		hotelDao.setTelephoneOfHotel(currentHotel.getHotelId(), updateRequest.getField());
		return hotelDao.findById(currentHotel.getHotelId()).get();
	}

	private boolean isEmailExists(String email) {
		return customerDao.findByEmail(email).isPresent() || hotelDao.findByHotelEmail(email).isPresent();
	}

	private Hotel getCurrentLoggedInHotel() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		return hotelDao.findByHotelEmail(email).get();
	}

	private Customer getCurrentLoggedInCustomer() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		return customerDao.findByEmail(email).get();
	}

	private boolean hotelWithNameAlreadyExitsInYourCity(String name, Address address) {
		Optional<Hotel> opt = hotelDao.findByNameAndAddress(name, address);
		if (opt.isPresent())
			return true;
		return false;
	}

	private Hotel buildHotel(HotelDTO hotelDTO) {
		return Hotel.builder().name(hotelDTO.getName()).hotelEmail(hotelDTO.getHotelEmail())
				.hotelPhone(hotelDTO.getHotelPhone()).hotelTelephone(hotelDTO.getHotelTelephone())
				.password(passwordEncoder.encode(hotelDTO.getPassword())).role(Role.ROLE_HOTEL)
				.hotelType(hotelDTO.getHotelType()).address(hotelDTO.getAddress()).build();
	}

}
