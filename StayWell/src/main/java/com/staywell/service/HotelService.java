package com.staywell.service;

import java.util.List;

import com.staywell.dto.HotelDTO;
import com.staywell.dto.UpdateHotelDetailsDTO;
import com.staywell.model.Hotel;

public interface HotelService {

	public Hotel registerHotel(HotelDTO hotelRequest);
	
	public Hotel getHotelById(Long id);
	
	public Hotel updateEmail(UpdateHotelDetailsDTO updateRequest);
	
	public Hotel updateName(UpdateHotelDetailsDTO updateRequest);
	
	public Hotel updatePhone(UpdateHotelDetailsDTO updateRequest);
	
	public Hotel updateTelephone(UpdateHotelDetailsDTO updateRequest);
	
	public boolean deactivateHotelAccount();
	
	public List<Hotel> getHotelsNearMe();
	
	public List<Hotel> getHotelsInCity(String city);
	
}
