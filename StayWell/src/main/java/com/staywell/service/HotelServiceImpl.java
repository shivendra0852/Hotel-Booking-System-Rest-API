package com.staywell.service;


import com.staywell.model.Hotel;

public class HotelServiceImpl implements HotelService{

	@Override
	public Hotel registerHotel(Hotel hotel) {
		
		return null;
	}
	
	
	private boolean passwordValidation(String password) {		
		String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";
		return password.matches(pattern);
	}

	@Override
	public Hotel getHotelById(Long id) {
		return null;
	}

}
