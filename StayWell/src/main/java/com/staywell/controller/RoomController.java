package com.staywell.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.staywell.dto.RoomDTO;
import com.staywell.exception.RoomException;
import com.staywell.model.DateDTO;
import com.staywell.model.Hotel;
import com.staywell.model.Room;
import com.staywell.service.RoomService;

import jakarta.validation.Valid;

@RestController
public class RoomController {
	
	@Autowired
	private RoomService roomService;
	
	@PostMapping("")
	public ResponseEntity<Room> addRoom(@Valid @RequestBody Room room) throws RoomException{
		
		ResponseEntity<Room> responseEntity = new ResponseEntity<>(roomService.addRoom(room), HttpStatus.CREATED);
			
		return responseEntity;
		
	}
	
	@PutMapping("/{roomNo}")
	public ResponseEntity<Room> updateRoom(@PathVariable Integer roomNo, @Valid @RequestBody RoomDTO roomDTO) throws RoomException{
		
		ResponseEntity<Room> responseEntity = new ResponseEntity<>(roomService.updateRoom(roomNo, roomDTO), HttpStatus.ACCEPTED);
		
		return responseEntity;
		
	}
	
	@DeleteMapping("")
	public ResponseEntity<String> deleteRoom(@PathVariable Integer roomNo) throws RoomException{
		
		ResponseEntity<String> responseEntity = new ResponseEntity<>(roomService.removeRoom(roomNo), HttpStatus.OK);
		
		return responseEntity;
		
	}
	
	@GetMapping("/{hoteId}")
	public ResponseEntity<List<Room>> getAllAvailableRoomsByHotelId(@PathVariable Long hoteId, @Valid @RequestBody DateDTO dateDTO) throws RoomException{
		
		LocalDate checkIn = dateDTO.getCheckIn();
		LocalDate checkOut = dateDTO.getCheckOut();
		
		ResponseEntity<List<Room>> responseEntity = new ResponseEntity<>(roomService.getAllAvailableRoomsByHotelId(hoteId, checkIn, checkOut), HttpStatus.OK);
		
		return responseEntity;
		
	}
	
	@GetMapping("")
	public ResponseEntity<List<Hotel>> getHotelsNearMe() {
		
		ResponseEntity<List<Hotel>> responseEntity = new ResponseEntity<>(roomService.getHotelsNearMe(), HttpStatus.OK);
		
		return responseEntity;
		
	}
	
	@GetMapping("/{city}")
	public ResponseEntity<List<Hotel>> getHotelsInCity(@PathVariable String city) {
		
		ResponseEntity<List<Hotel>> responseEntity = new ResponseEntity<>(roomService.getHotelsInCity(city), HttpStatus.OK);
		
		return responseEntity;
		
	}
	
}
