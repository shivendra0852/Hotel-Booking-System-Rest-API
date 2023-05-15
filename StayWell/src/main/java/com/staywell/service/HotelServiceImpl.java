package com.staywell.service;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.staywell.dto.HotelDTO;
import com.staywell.enums.HotelType;
import com.staywell.enums.Role;
import com.staywell.exception.HotelException;
import com.staywell.model.Hotel;
import com.staywell.model.Reservation;
import com.staywell.repository.HotelDao;

import jakarta.validation.ValidationException;


@Service
public class HotelServiceImpl implements HotelService{

	@Autowired
	private HotelDao hotelRepo;
	
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
		return hotelRepo.save(hotel);
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
       Optional<Hotel> existence = hotelRepo.findById(id);
       if(existence.isPresent()) return existence.get();
       throw new HotelException("No hotel found with the id "+id);
	}


	@Override
	public Hotel updateEmail(String email) {
		return hotelRepo.setEmailOfHotel(hotelId, email);
	}


	@Override
	public Hotel updateName(String name) {
       return hotelRepo.setNameOfHotel(hotelId, name);
	}


	@Override
	public Hotel updatePhone(String phone) {
		return hotelRepo.setPhoneOfHotel(hotelId, phone);
	}


	@Override
	public Hotel updateTelephone(String telephone) {
		return hotelRepo.setTelephoneOfHotel(hotelId, telephone);
	}


	@Override
	public boolean deactivateHotelAccount() {
		/*find hotel by id*/
		Hotel hotel = hotelRepo.findById(hotelId).get();
		
		List<Reservation> activeOrBookedReservations = new ArrayList<>();
		/*find reservations who are booked for future / currently active*/
		for(Reservation reservation:hotel.getReservations()) {
			if(reservation.getCheckinDate().isAfter(LocalDate.now())) activeOrBookedReservations.add(reservation);
		}
		
		/*If there is no reservations found which are booked then delete hotel and return true*/
		if(activeOrBookedReservations.size() == 0) {
			hotelRepo.deleteById(hotelId);
			return true;
		}
		throw new HotelException("Hotel "+hotel.getName()+" has reservations booked for future please serve/cancel those reservations before deleting account");
	}
	
	

}
