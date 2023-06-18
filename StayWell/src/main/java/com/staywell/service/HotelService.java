package com.staywell.service;

import java.util.List;

import com.staywell.dto.HotelDTO;
import com.staywell.dto.UpdateDetailsDTO;
import com.staywell.model.Hotel;

public interface HotelService {

	public Hotel registerHotel(HotelDTO hotelRequest);

	public String updateName(UpdateDetailsDTO updateRequest);

	public String updatePhone(UpdateDetailsDTO updateRequest);

	public String updateTelephone(UpdateDetailsDTO updateRequest);

	public String updateHotelType(UpdateDetailsDTO updateRequest);

	public Hotel getHotelById(Long id);

	public List<Hotel> getHotelsNearMe();

	public List<Hotel> getHotelsInCity(String city);

}
