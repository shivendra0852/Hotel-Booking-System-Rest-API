package com.staywell.service;

import com.staywell.model.Hotel;

public interface HotelService {

	public Hotel registerHotel(Hotel hotel);
	
	public Hotel getHotelById(Long id);
	
	
}
