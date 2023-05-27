package com.staywell.service;

import java.time.LocalDate;
import java.util.List;

import com.staywell.dto.RoomDTO;
import com.staywell.exception.RoomException;
import com.staywell.model.Room;

public interface RoomService {

	public Room addRoom(Room room) throws RoomException;
	
	public Room updateRoom(Integer roomId, RoomDTO roomDTO) throws RoomException;
	
	public String removeRoom(Integer roomId) throws RoomException;
	
	public List<Room> getAllAvailableRoomsByHotelId(Long hotelId, LocalDate checkIn, LocalDate checkOut) throws RoomException;
	
	public List<Room> getAllRoomsByHotel() throws RoomException;
	
}
