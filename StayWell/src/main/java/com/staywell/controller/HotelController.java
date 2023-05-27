package com.staywell.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.staywell.dto.HotelDTO;
import com.staywell.dto.UpdateHotelDetailsDTO;
import com.staywell.model.Hotel;
import com.staywell.service.HotelService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/staywell/hotels")
public class HotelController {

	
	@Autowired
	private HotelService hotelService;
	
	@PostMapping("/register")
	public ResponseEntity<Hotel> registerHotel(@Valid @RequestBody HotelDTO hotelRequest){	
		return new ResponseEntity<Hotel>(hotelService.registerHotel(hotelRequest), HttpStatus.CREATED);		
	}

	@GetMapping("/hotel/{hotelId}")
	public ResponseEntity<Hotel> getHotelById(@PathVariable("hotelId") long hotelId){
		return new ResponseEntity<Hotel>(hotelService.getHotelById(hotelId), HttpStatus.FOUND);
	}
	@GetMapping("/update-name")
	public ResponseEntity<Hotel> updateName(@RequestBody UpdateHotelDetailsDTO updateDetailsRequest){
		return new ResponseEntity<Hotel>(hotelService.updateName(updateDetailsRequest), HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/update-email")
	public ResponseEntity<Hotel> updateEmail(@RequestBody UpdateHotelDetailsDTO updateDetailsRequest){
		return new ResponseEntity<Hotel>(hotelService.updateEmail(updateDetailsRequest), HttpStatus.ACCEPTED);
	}
	@GetMapping("/update-phone")
	public ResponseEntity<Hotel> updatePhone(@RequestBody UpdateHotelDetailsDTO updateDetailsRequest){
		return new ResponseEntity<Hotel>(hotelService.updatePhone(updateDetailsRequest), HttpStatus.ACCEPTED);
	}
	@GetMapping("/update-telephone")
	public ResponseEntity<Hotel> updateTelephone(@RequestBody UpdateHotelDetailsDTO updateDetailsRequest){
		return new ResponseEntity<Hotel>(hotelService.updateTelephone(updateDetailsRequest), HttpStatus.ACCEPTED);
	}
	
	@GetMapping("near-me")
	public ResponseEntity<List<Hotel>> getHotelsNearMe() {
		ResponseEntity<List<Hotel>> responseEntity = new ResponseEntity<>(hotelService.getHotelsNearMe(), HttpStatus.FOUND);
		return responseEntity;		
	}
	
	@GetMapping("in-city/{city}")
	public ResponseEntity<List<Hotel>> getHotelsInCity(@PathVariable String city) {
		ResponseEntity<List<Hotel>> responseEntity = new ResponseEntity<>(hotelService.getHotelsInCity(city), HttpStatus.FOUND);
		return responseEntity;		
	}
	
}
