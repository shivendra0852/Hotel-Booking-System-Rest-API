package com.staywell.service;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.staywell.dto.HotelDTO;
import com.staywell.enums.Role;
import com.staywell.exception.HotelException;
import com.staywell.model.Address;
import com.staywell.model.Customer;
import com.staywell.model.Hotel;
import com.staywell.repository.CustomerDao;
import com.staywell.repository.HotelDao;

import jakarta.validation.ValidationException;


@Service
public class HotelServiceImpl implements HotelService{

	@Autowired
	private HotelDao hotelDao;
	
	@Autowired
	private CustomerDao customerDao;
	
	private Long hotelId = null;/*Extract id of the current logged in hotel from spring security context holder*/
	
	@Override
	public Hotel registerHotel(HotelDTO hotelRequest) {
		
		/*Validating password under some constraints*/
		if(!passwordValidation(hotelRequest.getPassword())) throw new ValidationException("Password is not valid!");
		
		/*Creating hotel object and mapping attributes from request dto to hotel entity*/
		Hotel hotel = new Hotel();
		hotel.setName(hotelRequest.getName());
		hotel.setHotelEmail(hotelRequest.getEmail());
		hotel.setHotelPhone(hotelRequest.getPhone());
		hotel.setHotelTelephone(hotelRequest.getTelephone());
		hotel.setPassword(encryptPassword(hotelRequest.getPassword()));
		hotel.setHotelType(hotelRequest.getType());
		hotel.setRole(Role.HOTEL.toString());
		hotel.setHotel_address(hotelRequest.getAddress());
		
		
		/*Saving to the database*/
		return hotelDao.save(hotel);
	}
	
	
	private boolean passwordValidation(String password) {		
		String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";
		return password.matches(pattern);
	}

	
	/*Password encoder*/
	private String encryptPassword(String password) {
		return new BCryptPasswordEncoder().encode(password);
	}

	@Override
	public Hotel getHotelById(Long id) {
       Optional<Hotel> existence = hotelDao.findById(id);
       if(existence.isPresent()) return existence.get();
       throw new HotelException("No hotel found with the id "+id);
	}


	@Override
	public Hotel updateEmail(String email) {
		return hotelDao.setEmailOfHotel(hotelId, email);
	}


	@Override
	public Hotel updateName(String name) {
       return hotelDao.setNameOfHotel(hotelId, name);
	}


	@Override
	public Hotel updatePhone(String phone) {
		return hotelDao.setPhoneOfHotel(hotelId, phone);
	}


	@Override
	public Hotel updateTelephone(String telephone) {
		return hotelDao.setTelephoneOfHotel(hotelId, telephone);
	}


	@Override
	public boolean deactivateHotelAccount() {
	  hotelDao.deleteById(hotelId);
	  return true;
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

}
