package com.staywell.service;

import java.util.List;

import com.staywell.dto.request.HotelRequest;
import com.staywell.dto.request.UpdateRequest;
import com.staywell.model.Hotel;

public interface HotelService {

	public Hotel registerHotel(HotelRequest hotelRequest);

	public String updateName(UpdateRequest updateRequest);
	
	public String updatePassword(UpdateRequest updateRequest);

	public String updatePhone(UpdateRequest updateRequest);

	public String updateTelephone(UpdateRequest updateRequest);

	public String updateHotelType(UpdateRequest updateRequest);

	public Hotel getHotelById(Long id);

	public List<Hotel> getHotelsNearMe();

	public List<Hotel> getHotelsInCity(String city);

}
