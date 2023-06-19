package com.staywell.service;

import java.time.LocalDate;
import java.util.List;

import com.staywell.dto.RoomDTO;
import com.staywell.dto.UpdateDetailsDTO;
import com.staywell.model.Room;

public interface RoomService {

	public Room addRoom(RoomDTO roomDTO);

	public String updateRoomType(UpdateDetailsDTO updateRequest, Long roomId);

	public String updateNoOfPerson(UpdateDetailsDTO updateRequest, Long roomId);

	public String updatePrice(UpdateDetailsDTO updateRequest, Long roomId);

	public String updateAvailable(UpdateDetailsDTO updateRequest, Long roomId);

	public String removeRoom(UpdateDetailsDTO updateRequest);

	public List<Room> getAllAvailableRoomsByHotelId(Long hotelId, LocalDate checkIn, LocalDate checkOut);

	public List<Room> getAllRoomsByHotel();

}
