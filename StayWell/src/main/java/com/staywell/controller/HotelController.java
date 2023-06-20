package com.staywell.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.staywell.dto.request.HotelRequest;
import com.staywell.dto.request.UpdateRequest;
import com.staywell.model.Hotel;
import com.staywell.service.HotelService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/staywell/hotels")
@AllArgsConstructor
public class HotelController {

	private HotelService hotelService;

	@PostMapping("/register")
	public ResponseEntity<Hotel> registerHotel(@Valid @RequestBody HotelRequest hotelRequest) {
		return new ResponseEntity<Hotel>(hotelService.registerHotel(hotelRequest), HttpStatus.CREATED);
	}

	@PutMapping("/update-name")
	public ResponseEntity<String> updateName(@RequestBody UpdateRequest updateDetailsRequest) {
		return new ResponseEntity<>(hotelService.updateName(updateDetailsRequest), HttpStatus.ACCEPTED);
	}

	@PutMapping("/update-password")
	public ResponseEntity<String> updatePassword(@RequestBody UpdateRequest updateRequest) {
		String res = hotelService.updatePassword(updateRequest);
		return new ResponseEntity<>(res, HttpStatus.OK);
	}

	@PutMapping("/update-phone")
	public ResponseEntity<String> updatePhone(@RequestBody UpdateRequest updateDetailsRequest) {
		return new ResponseEntity<>(hotelService.updatePhone(updateDetailsRequest), HttpStatus.ACCEPTED);
	}

	@PutMapping("/update-telephone")
	public ResponseEntity<String> updateTelephone(@RequestBody UpdateRequest updateDetailsRequest) {
		return new ResponseEntity<>(hotelService.updateTelephone(updateDetailsRequest), HttpStatus.ACCEPTED);
	}

	@PutMapping("/update-hoteltype")
	public ResponseEntity<String> updateHotelType(@RequestBody UpdateRequest updateDetailsRequest) {
		return new ResponseEntity<>(hotelService.updateHotelType(updateDetailsRequest), HttpStatus.ACCEPTED);
	}

	@GetMapping("/get-hotel-by-id/{hotelId}")
	public ResponseEntity<Hotel> getHotelById(@PathVariable("hotelId") long hotelId) {
		return new ResponseEntity<Hotel>(hotelService.getHotelById(hotelId), HttpStatus.FOUND);
	}

	@GetMapping("/near-me")
	public ResponseEntity<List<Hotel>> getHotelsNearMe() {
		ResponseEntity<List<Hotel>> responseEntity = new ResponseEntity<>(hotelService.getHotelsNearMe(),
				HttpStatus.FOUND);
		return responseEntity;
	}

	@GetMapping("/in-city/{city}")
	public ResponseEntity<List<Hotel>> getHotelsInCity(@PathVariable String city) {
		ResponseEntity<List<Hotel>> responseEntity = new ResponseEntity<>(hotelService.getHotelsInCity(city),
				HttpStatus.FOUND);
		return responseEntity;
	}

}
