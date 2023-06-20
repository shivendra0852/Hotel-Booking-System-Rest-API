package com.staywell.service;

import java.time.LocalDate;
import java.util.List;

import com.staywell.dto.request.RoomRequest;
import com.staywell.dto.request.UpdateRequest;
import com.staywell.model.Room;

public interface RoomService {

	public Room addRoom(RoomRequest roomRequest);

	public String updateRoomType(UpdateRequest updateRequest, Long roomId);

	public String updateNoOfPerson(UpdateRequest updateRequest, Long roomId);

	public String updatePrice(UpdateRequest updateRequest, Long roomId);

	public String updateAvailable(UpdateRequest updateRequest, Long roomId);

	public String removeRoom(UpdateRequest updateRequest);

	public List<Room> getAllAvailableRoomsByHotelId(Long hotelId, LocalDate checkIn, LocalDate checkOut);

	public List<Room> getAllRoomsByHotel();

}
