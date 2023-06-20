package com.staywell.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.staywell.dto.request.HotelRequest;
import com.staywell.dto.request.UpdateRequest;
import com.staywell.enums.HotelType;
import com.staywell.enums.Role;
import com.staywell.exception.CustomerException;
import com.staywell.exception.HotelException;
import com.staywell.model.Address;
import com.staywell.model.Customer;
import com.staywell.model.Hotel;
import com.staywell.repository.CustomerDao;
import com.staywell.repository.HotelDao;
import com.staywell.service.HotelService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
@Transactional
public class HotelServiceImpl implements HotelService {

	private HotelDao hotelDao;
	private CustomerDao customerDao;
	private PasswordEncoder passwordEncoder;

	@Override
	public Hotel registerHotel(HotelRequest hotelRequest) {

		log.info("Performing email validation");
		if (isEmailExists(hotelRequest.getHotelEmail())) {
			throw new HotelException("This email is already registered. Please use a different email to register.");
		}

		log.info("Verifying Hotel name");
		if (hotelWithNameAlreadyExitsInYourCity(hotelRequest.getName(), hotelRequest.getAddress())) {
			throw new HotelException("Hotel already exits in your city with name : " + hotelRequest.getName());
		}

		Hotel hotel = buildHotel(hotelRequest);
		hotelDao.save(hotel);

		log.info("Registration successfull");
		return hotel;
	}

	@Override
	public String updateName(UpdateRequest updateRequest) {
		Hotel currentHotel = getCurrentLoggedInHotel();

		log.info("Verifying credentials");
		String password = updateRequest.getPassword();
		if (!passwordEncoder.matches(password, currentHotel.getPassword())) {
			throw new HotelException("Wrong credentials!");
		}
		hotelDao.setNameOfHotel(currentHotel.getHotelId(), updateRequest.getField());

		log.info("Updation successfull");
		return "Updated hotel name successfully";
	}

	@Override
	public String updatePassword(UpdateRequest updateRequest) {
		Hotel currentHotel = getCurrentLoggedInHotel();

		log.info("Verifying credentials");
		String password = updateRequest.getPassword();
		if (!passwordEncoder.matches(password, currentHotel.getPassword())) {
			throw new HotelException("Wrong credentials!");
		}
		hotelDao.setHotelPassword(currentHotel.getHotelId(),
				passwordEncoder.encode(updateRequest.getField()));

		log.info("Updation successfull");
		return "Password updated successfully!";
	}

	@Override
	public String updatePhone(UpdateRequest updateRequest) {
		Hotel currentHotel = getCurrentLoggedInHotel();

		log.info("Verifying credentials");
		String password = updateRequest.getPassword();
		if (!passwordEncoder.matches(password, currentHotel.getPassword())) {
			throw new HotelException("Wrong credentials!");
		}
		hotelDao.setPhoneOfHotel(currentHotel.getHotelId(), updateRequest.getField());

		log.info("Updation successfull");
		return "Updated hotel phone successfully";
	}

	@Override
	public String updateTelephone(UpdateRequest updateRequest) {
		Hotel currentHotel = getCurrentLoggedInHotel();

		log.info("Verifying credentials");
		String password = updateRequest.getPassword();
		if (!passwordEncoder.matches(password, currentHotel.getPassword())) {
			throw new HotelException("Wrong credentials!");
		}
		hotelDao.setTelephoneOfHotel(currentHotel.getHotelId(), updateRequest.getField());

		log.info("Updation successfull");
		return "Updated hotel telephone successfully";
	}

	@Override
	public String updateHotelType(UpdateRequest updateRequest) {
		Hotel hotel = getCurrentLoggedInHotel();

		log.info("Verifying credentials");
		if (!passwordEncoder.matches(updateRequest.getPassword(), hotel.getPassword())) {
			throw new HotelException("Wrong credentials!");
		}
		hotelDao.setHotelType(hotel.getHotelId(), HotelType.valueOf(updateRequest.getField()));

		log.info("Updation successfull");
		return "Updated hotel type successfully";
	}

	@Override
	public Hotel getHotelById(Long id) {
		Optional<Hotel> optional = hotelDao.findById(id);
		if (optional.isPresent())
			return optional.get();
		throw new HotelException("No hotel found with id " + id);
	}

	@Override
	public List<Hotel> getHotelsNearMe() {
		Customer customer = getCurrentLoggedInCustomer();
		List<Hotel> hotels = hotelDao.getHotelByCity(customer.getAddress().getCity());
		if (hotels.isEmpty())
			throw new HotelException("Hotels Not Found In Your Area!");
		return hotels;
	}

	@Override
	public List<Hotel> getHotelsInCity(String city) {
		List<Hotel> hotels = hotelDao.getHotelByCity(city);
		if (hotels.isEmpty())
			throw new HotelException("Hotels Not Found In Your Area!");
		return hotels;
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
		Optional<Hotel> opt = hotelDao.getHotelByNameAndCity(name, address.getCity());
		if (opt.isPresent())
			return true;
		return false;
	}

	private Hotel buildHotel(HotelRequest hotelRequest) {
		return Hotel.builder()
				.name(hotelRequest.getName())
				.hotelEmail(hotelRequest.getHotelEmail())
				.hotelPhone(hotelRequest.getHotelPhone())
				.hotelTelephone(hotelRequest.getHotelTelephone())
				.password(passwordEncoder.encode(hotelRequest.getPassword()))
				.role(Role.ROLE_HOTEL)
				.hotelType(hotelRequest.getHotelType())
				.address(hotelRequest.getAddress())
				.amenities(new ArrayList<>()).rooms(new ArrayList<>()).reservations(new ArrayList<>()).feedbacks(new ArrayList<>())
				.build();
	}

}
