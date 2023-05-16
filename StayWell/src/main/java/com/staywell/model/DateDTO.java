package com.staywell.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.FutureOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DateDTO {

	@FutureOrPresent
	@JsonFormat(pattern = "yyy-MM-dd")
	LocalDate checkIn;
	
	@JsonFormat(pattern = "yyy-MM-dd")
	@FutureOrPresent
	LocalDate checkOut;
	
}
