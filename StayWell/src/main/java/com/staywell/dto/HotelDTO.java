package com.staywell.dto;

import com.staywell.enums.HotelType;
import com.staywell.model.Address;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelDTO {

	@NotNull @NotEmpty @NotBlank
	private String name;

	@NotNull @NotEmpty @NotBlank
	private String hotelEmail;

	@NotNull @NotEmpty @NotBlank
	private String hotelPhone;

	@NotNull @NotEmpty @NotBlank
	private String hotelTelephone;

	@NotNull @NotEmpty @NotBlank @Size(min = 8)
	@Pattern(regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}")
	private String password;

	@Valid
	private Address address;

	@NotNull @NotEmpty @NotBlank
	@Enumerated(EnumType.STRING)
	private HotelType hotelType;

}
