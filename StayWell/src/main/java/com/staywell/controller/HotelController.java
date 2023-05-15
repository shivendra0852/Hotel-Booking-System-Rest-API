package com.staywell.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.staywell.dto.HotelDTO;
import com.staywell.model.Hotel;
import com.staywell.service.HotelService;

@RestController
public class HotelController {

	
	@Autowired
	private HotelService hotelService;
	
	@PostMapping("/hotel/register")
	public ResponseEntity<Hotel> registerHotel(HotelDTO hotelRequest){	
		return new ResponseEntity<Hotel>(hotelService.registerHotel(hotelRequest), HttpStatus.CREATED);		
	}

	@GetMapping("/hotel")
	public ResponseEntity<Hotel> getHotelById(@RequestParam("hotelId") long hotelId){
		return new ResponseEntity<Hotel>(hotelService.getHotelById(hotelId), HttpStatus.FOUND);
	}
	@GetMapping("/hotel/update")
	public ResponseEntity<Hotel> updateName(@RequestParam("name") String name){
		return new ResponseEntity<Hotel>(hotelService.updateName(name), HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/hotel/update")
	public ResponseEntity<Hotel> updateEmail(@RequestParam("email") String email){
		return new ResponseEntity<Hotel>(hotelService.updateEmail(email), HttpStatus.ACCEPTED);
	}
	@GetMapping("/hotel/update")
	public ResponseEntity<Hotel> updatePhone(@RequestParam("phone") String phone){
		return new ResponseEntity<Hotel>(hotelService.updatePhone(phone), HttpStatus.ACCEPTED);
	}
	@GetMapping("/hotel/update")
	public ResponseEntity<Hotel> updateTelephone(@RequestParam("telephone") String telephone){
		return new ResponseEntity<Hotel>(hotelService.updateTelephone(telephone), HttpStatus.ACCEPTED);
	}
	
	
}
