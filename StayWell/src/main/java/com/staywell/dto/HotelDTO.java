package com.staywell.dto;

import com.staywell.enums.HotelType;
import com.staywell.model.Address;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelDTO {

	private String name;
	private String email;
	private String phone;
	private String telephone;
	private String password;
	private HotelType type;
	private Address address;
	
	
}
