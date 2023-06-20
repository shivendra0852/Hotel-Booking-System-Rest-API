package com.staywell.controller;

import java.util.List;

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

import com.staywell.dto.request.DateRequest;
import com.staywell.dto.request.RoomRequest;
import com.staywell.dto.request.UpdateRequest;
import com.staywell.dto.response.RoomResponse;
import com.staywell.exception.RoomException;
import com.staywell.service.RoomService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/staywell/rooms")
@AllArgsConstructor
public class RoomController {

	private RoomService roomService;

	@PostMapping("/add")
	public ResponseEntity<RoomResponse> addRoom(@Valid @RequestBody RoomRequest roomRequest) {
		ResponseEntity<RoomResponse> responseEntity = new ResponseEntity<>(roomService.addRoom(roomRequest),
				HttpStatus.CREATED);
		return responseEntity;
	}

	@PutMapping("/update/roomtype/{roomId}")
	public ResponseEntity<String> updateRoomType(@RequestBody UpdateRequest updateDetailsRequest,
			@PathVariable("roomId") Long roomId) {
		return new ResponseEntity<String>(roomService.updateRoomType(updateDetailsRequest, roomId),
				HttpStatus.ACCEPTED);
	}

	@PutMapping("/update/no-of-person/{roomId}")
	public ResponseEntity<String> updateNoOfPerson(@RequestBody UpdateRequest updateDetailsRequest,
			@PathVariable("roomId") Long roomId) {
		return new ResponseEntity<String>(roomService.updateNoOfPerson(updateDetailsRequest, roomId),
				HttpStatus.ACCEPTED);
	}

	@PutMapping("/update/price/{roomId}")
	public ResponseEntity<String> updatePrice(@RequestBody UpdateRequest updateDetailsRequest,
			@PathVariable("roomId") Long roomId) {
		return new ResponseEntity<String>(roomService.updatePrice(updateDetailsRequest, roomId), HttpStatus.ACCEPTED);
	}

	@PutMapping("/update/availability/{roomId}")
	public ResponseEntity<String> updateAvailable(@RequestBody UpdateRequest updateDetailsRequest,
			@PathVariable("roomId") Long roomId) {
		return new ResponseEntity<String>(roomService.updateAvailable(updateDetailsRequest, roomId),
				HttpStatus.ACCEPTED);
	}

	@DeleteMapping("/delete")
	public ResponseEntity<String> deleteRoom(@RequestBody UpdateRequest updateRequest) {
		ResponseEntity<String> responseEntity = new ResponseEntity<>(roomService.removeRoom(updateRequest),
				HttpStatus.OK);
		return responseEntity;
	}

	@GetMapping("/get/available/{hotelId}")
	public ResponseEntity<List<RoomResponse>> getAllAvailableRoomsByHotelId(@PathVariable("hotelId") Long hoteId,
			@Valid @RequestBody DateRequest dateRequest) throws RoomException {
		ResponseEntity<List<RoomResponse>> responseEntity = new ResponseEntity<>(
				roomService.getAllAvailableRoomsByHotelId(hoteId, dateRequest), HttpStatus.FOUND);
		return responseEntity;
	}

	@GetMapping("/get/all")
	public ResponseEntity<List<RoomResponse>> getAllAvailableRoomsByHotel() throws RoomException {
		ResponseEntity<List<RoomResponse>> responseEntity = new ResponseEntity<>(roomService.getAllRoomsByHotel(),
				HttpStatus.FOUND);
		return responseEntity;
	}

}
