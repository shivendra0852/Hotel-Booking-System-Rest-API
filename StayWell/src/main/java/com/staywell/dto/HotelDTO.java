package com.staywell.dto;

import com.staywell.enums.HotelType;
import com.staywell.model.Address;

import ch.qos.logback.core.subst.Token.Type;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelDTO {

	@NotNull
	@NotEmpty
	private String name;
	@Column(unique = true)
	@Email
	@NotNull
	private String hotelEmail;
	@NotNull
	@NotEmpty
	private String hotelPhone;
	@NotNull
	@NotEmpty
	private String hotelTelephone;
	
	@Pattern(regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}")
	@NotNull
	@NotEmpty
	private String password;
	
	
	@Embedded
	@Valid
	private Address address;
	
	@Enumerated(EnumType.STRING)
	private HotelType hotelType;
	
	
}
