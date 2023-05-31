package com.staywell.service;

import java.util.List;

import com.staywell.dto.HotelDTO;
import com.staywell.dto.UpdateDetailsDTO;
import com.staywell.model.Hotel;

public interface HotelService {

	public Hotel registerHotel(HotelDTO hotelRequest);
	
	public Hotel getHotelById(Long id);
	
	public Hotel updateEmail(UpdateDetailsDTO updateRequest);
	
	public Hotel updateName(UpdateDetailsDTO updateRequest);
	
	public Hotel updatePhone(UpdateDetailsDTO updateRequest);
	
	public Hotel updateTelephone(UpdateDetailsDTO updateRequest);
	
	public boolean deactivateHotelAccount();
	
	public List<Hotel> getHotelsNearMe();
	
	public List<Hotel> getHotelsInCity(String city);
	
}
