package com.staywell.service;

import java.time.LocalDate;
import java.util.List;

import com.staywell.dto.RoomDTO;
import com.staywell.dto.UpdateDetailsDTO;
import com.staywell.exception.RoomException;
import com.staywell.model.Room;

public interface RoomService {

	public Room addRoom(RoomDTO roomDTO) throws RoomException;
	
	public String updateRoomType(UpdateDetailsDTO updateRequest, Integer roomId);
	
	public String updateNoOfPerson(UpdateDetailsDTO updateRequest, Integer roomId);
	
	public String updatePrice(UpdateDetailsDTO updateRequest, Integer roomId);
	
	public String updateAvailable(UpdateDetailsDTO updateRequest, Integer roomId);
	
	public String removeRoom(Integer roomId) throws RoomException;
	
	public List<Room> getAllAvailableRoomsByHotelId(Long hotelId, LocalDate checkIn, LocalDate checkOut) throws RoomException;
	
	public List<Room> getAllRoomsByHotel() throws RoomException;
	
}
