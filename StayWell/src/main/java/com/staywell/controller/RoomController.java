package com.staywell.controller;

import java.time.LocalDate;
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

import com.staywell.dto.DateDTO;
import com.staywell.dto.RoomDTO;
import com.staywell.dto.UpdateDetailsDTO;
import com.staywell.exception.RoomException;
import com.staywell.model.Room;
import com.staywell.service.RoomService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/staywell/rooms")
public class RoomController {

	private RoomService roomService;

	@PostMapping("/add")
	public ResponseEntity<Room> addRoom(@Valid @RequestBody RoomDTO roomDTO) throws RoomException {
		ResponseEntity<Room> responseEntity = new ResponseEntity<>(roomService.addRoom(roomDTO), HttpStatus.CREATED);
		return responseEntity;
	}

	@PutMapping("/update-room-type/{roomId}")
	public ResponseEntity<String> updateRoomType(@RequestBody UpdateDetailsDTO updateDetailsRequest,
			@PathVariable("roomId") Long roomId) {
		return new ResponseEntity<String>(roomService.updateRoomType(updateDetailsRequest, roomId),
				HttpStatus.ACCEPTED);
	}

	@PutMapping("/update-no-of-person/{roomId}")
	public ResponseEntity<String> updateNoOfPerson(@RequestBody UpdateDetailsDTO updateDetailsRequest,
			@PathVariable("roomId") Long roomId) {
		return new ResponseEntity<String>(roomService.updateNoOfPerson(updateDetailsRequest, roomId),
				HttpStatus.ACCEPTED);
	}

	@PutMapping("/update-price/{roomId}")
	public ResponseEntity<String> updatePrice(@RequestBody UpdateDetailsDTO updateDetailsRequest,
			@PathVariable("roomId") Long roomId) {
		return new ResponseEntity<String>(roomService.updatePrice(updateDetailsRequest, roomId), HttpStatus.ACCEPTED);
	}

	@PutMapping("/update-availability/{roomId}")
	public ResponseEntity<String> updateAvailable(@RequestBody UpdateDetailsDTO updateDetailsRequest,
			@PathVariable("roomId") Long roomId) {
		return new ResponseEntity<String>(roomService.updateAvailable(updateDetailsRequest, roomId),
				HttpStatus.ACCEPTED);
	}

	@DeleteMapping("/delete")
	public ResponseEntity<String> deleteRoom(@RequestBody UpdateDetailsDTO updateRequest) throws RoomException {
		ResponseEntity<String> responseEntity = new ResponseEntity<>(roomService.removeRoom(updateRequest),
				HttpStatus.OK);
		return responseEntity;
	}

	@GetMapping("/get-available-rooms/{hotelId}")
	public ResponseEntity<List<Room>> getAllAvailableRoomsByHotelId(@PathVariable("hotelId") Long hoteId,
			@Valid @RequestBody DateDTO dateDTO) throws RoomException {

		LocalDate checkIn = dateDTO.getCheckIn();
		LocalDate checkOut = dateDTO.getCheckOut();

		ResponseEntity<List<Room>> responseEntity = new ResponseEntity<>(
				roomService.getAllAvailableRoomsByHotelId(hoteId, checkIn, checkOut), HttpStatus.FOUND);
		return responseEntity;
	}

	@GetMapping("/get-all-rooms")
	public ResponseEntity<List<Room>> getAllAvailableRoomsByHotel() throws RoomException {
		ResponseEntity<List<Room>> responseEntity = new ResponseEntity<>(roomService.getAllRoomsByHotel(),
				HttpStatus.FOUND);
		return responseEntity;
	}

}
