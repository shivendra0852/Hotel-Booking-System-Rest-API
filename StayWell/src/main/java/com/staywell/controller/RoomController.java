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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.staywell.dto.RoomDTO;
import com.staywell.exception.RoomException;
import com.staywell.model.DateDTO;
import com.staywell.model.Room;
import com.staywell.service.RoomService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/staywell/rooms")
public class RoomController {
	
	@Autowired
	private RoomService roomService;
	
	@PostMapping("/add")
	public ResponseEntity<Room> addRoom(@Valid @RequestBody Room room) throws RoomException{
		
		ResponseEntity<Room> responseEntity = new ResponseEntity<>(roomService.addRoom(room), HttpStatus.CREATED);
			
		return responseEntity;
		
	}
	
	@PutMapping("/update/{roomNo}")
	public ResponseEntity<Room> updateRoom(@PathVariable Integer roomNo, @Valid @RequestBody RoomDTO roomDTO) throws RoomException{
		
		ResponseEntity<Room> responseEntity = new ResponseEntity<>(roomService.updateRoom(roomNo, roomDTO), HttpStatus.ACCEPTED);
		
		return responseEntity;
		
	}
	
	@DeleteMapping("/delete/{roomNo}")
	public ResponseEntity<String> deleteRoom(@PathVariable Integer roomNo) throws RoomException{
		
		ResponseEntity<String> responseEntity = new ResponseEntity<>(roomService.removeRoom(roomNo), HttpStatus.OK);
		
		return responseEntity;
		
	}
	
	@GetMapping("/get-rooms/{hoteId}")
	public ResponseEntity<List<Room>> getAllAvailableRoomsByHotelId(@PathVariable Long hoteId, @Valid @RequestBody DateDTO dateDTO) throws RoomException{
		
		LocalDate checkIn = dateDTO.getCheckIn();
		LocalDate checkOut = dateDTO.getCheckOut();
		
		ResponseEntity<List<Room>> responseEntity = new ResponseEntity<>(roomService.getAllAvailableRoomsByHotelId(hoteId, checkIn, checkOut), HttpStatus.FOUND);
		
		return responseEntity;
		
	}
	
}
