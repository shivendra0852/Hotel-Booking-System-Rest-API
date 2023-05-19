package com.staywell.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.FutureOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {

	@JsonFormat(pattern = "yyyy-MM-dd")
	@FutureOrPresent
	private LocalDate checkinDate;
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	@FutureOrPresent
	private LocalDate checkoutDate;
	
	private Integer noOfPerson;
	
}
