package com.staywell.service;

import java.time.LocalDateTime;
import java.util.List;

import com.staywell.dto.RoomDTO;
import com.staywell.exception.RoomException;
import com.staywell.model.Room;

public interface RoomService {

	public Room addRoom(Room room) throws RoomException;
	public Room updateRoom(Integer roomNo, RoomDTO roomDTO) throws RoomException;
	public String removeRoom(Integer roomNo) throws RoomException;
	public List<Room> getAllAvailableRoomsByHotelId(Integer hotelId, LocalDateTime checkIn, LocalDateTime checkOut);
	public List<Room> getAvailableRoomsNearMe(LocalDateTime checkIn, LocalDateTime checkOut);
	public List<Room> getAvailableRoomsInCity(String city, LocalDateTime checkIn, LocalDateTime checkOut);
	
}
