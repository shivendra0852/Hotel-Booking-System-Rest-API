package com.staywell.service;

import java.util.List;

import com.staywell.dto.request.DateRequest;
import com.staywell.dto.request.RoomRequest;
import com.staywell.dto.request.UpdateRequest;
import com.staywell.dto.response.RoomResponse;

public interface RoomService {

	public RoomResponse addRoom(RoomRequest roomRequest);

	public String updateRoomType(UpdateRequest updateRequest, Long roomId);

	public String updateNoOfPerson(UpdateRequest updateRequest, Long roomId);

	public String updatePrice(UpdateRequest updateRequest, Long roomId);

	public String updateAvailable(UpdateRequest updateRequest, Long roomId);

	public String removeRoom(UpdateRequest updateRequest);

	public List<RoomResponse> getAllAvailableRoomsByHotelId(Long hotelId, DateRequest dateRequest);

	public List<RoomResponse> getAllRoomsByHotel();

}
