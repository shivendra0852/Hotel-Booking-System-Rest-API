package com.staywell.service;

import java.util.List;

import com.staywell.dto.HotelDTO;
import com.staywell.model.Hotel;

public interface HotelService {

	public Hotel registerHotel(HotelDTO hotelRequest);
	
	public Hotel getHotelById(Long id);
	
	public Hotel updateEmail(String email);
	
	public Hotel updateName(String name);
	
	public Hotel updatePhone(String phone);
	
	public Hotel updateTelephone(String telephone);
	
	public boolean deactivateHotelAccount();
	
	public List<Hotel> getHotelsNearMe();
	
	public List<Hotel> getHotelsInCity(String city);
	
}
