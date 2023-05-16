package com.staywell.service;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

import jakarta.validation.ValidationException;


@Service
public class HotelServiceImpl implements HotelService{

	@Autowired
	private HotelDao hotelDao;
	
	@Autowired
	private CustomerDao customerDao;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private ReservationDao reservationDao;
	
	private Hotel hotel = LoggedInUser.getHotel();/*Extract id of the current logged in hotel from spring security context holder*/
	
	@Override
	public Hotel registerHotel(HotelDTO hotelRequest) {

		/*Checking if there exist a user or a hotel with the provided email*/
		if(ifEmailExists(hotelRequest.getEmail())) throw new HotelException("This email is already registered, Please use some other email to register.");
		
		/*Creating hotel object and mapping attributes from request dto to hotel entity*/
		Hotel hotel = new Hotel();
		hotel.setName(hotelRequest.getName());
		hotel.setHotelEmail(hotelRequest.getEmail());
		hotel.setHotelPhone(hotelRequest.getPhone());
		hotel.setHotelTelephone(hotelRequest.getTelephone());
		hotel.setPassword(passwordEncoder.encode(hotelRequest.getPassword()));
		hotel.setHotelType(hotelRequest.getType());
		hotel.setRole(Role.HOTEL.toString());
		hotel.setAddress(hotelRequest.getAddress());
		
		
		/*Saving to the database*/
		return hotelDao.save(hotel);
	}
	
	

	@Override
	public Hotel getHotelById(Long id) {
       Optional<Hotel> existence = hotelDao.findById(id);
       if(existence.isPresent()) return existence.get();
       throw new HotelException("No hotel found with the id "+id);
	}




	@Override
	public boolean deactivateHotelAccount() {
		reservationDao.updateReservationStatus(hotel);
		List<Reservation> reservation = reservationDao.getAllPendingReservations(hotel);
	    if(reservation.size() == 0) hotelDao.delete(hotel);
	    throw new HotelException("Hotel "+hotel.getName()+" has reservations booked for future please serve/cancel those reservations before deleting account");
	}
	
	@Override
	public List<Hotel> getHotelsNearMe() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		Customer customer = customerDao.findByEmail(email).get();
		List<Hotel> hotels = hotelDao.findByAddress(customer.getAddress());
		return hotels;
	}

	@Override
	public List<Hotel> getHotelsInCity(String city) {
		Address address = new Address();
		address.setCity(city);
		List<Hotel> hotels = hotelDao.findByAddress(address);
		return hotels;
	}
	
	private boolean ifEmailExists(String email) {
		return customerDao.findByEmail(email).isPresent() || hotelDao.findByHotelEmail(email).isPresent();
	}


	@Override
	public Hotel updateEmail(UpdateHotelDetailsDTO updateRequest) {
		// TODO Auto-generated method stub
		String password = passwordEncoder.encode(updateRequest.getPassword());
		if(!hotel.getPassword().equals(password)) throw new HotelException("Wrong credentials!");
		return hotelDao.setEmailOfHotel(hotel.getHotelId(), updateRequest.getField());
	}


	@Override
	public Hotel updateName(UpdateHotelDetailsDTO updateRequest) {
		String password = passwordEncoder.encode(updateRequest.getPassword());
		if(!hotel.getPassword().equals(password)) throw new HotelException("Wrong credentials!");
		return hotelDao.setNameOfHotel(hotel.getHotelId(), updateRequest.getField());
	}


	@Override
	public Hotel updatePhone(UpdateHotelDetailsDTO updateRequest) {
		String password = passwordEncoder.encode(updateRequest.getPassword());
		if(!hotel.getPassword().equals(password)) throw new HotelException("Wrong credentials!");
		return hotelDao.setPhoneOfHotel(hotel.getHotelId(), updateRequest.getField());
	}


	@Override
	public Hotel updateTelephone(UpdateHotelDetailsDTO updateRequest) {
		String password = passwordEncoder.encode(updateRequest.getPassword());
		if(!hotel.getPassword().equals(password)) throw new HotelException("Wrong credentials!");
		return hotelDao.setTelephoneOfHotel(hotel.getHotelId(), updateRequest.getField());
	}

}
