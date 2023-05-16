package com.staywell.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {

	private LocalDate checkinDate;
	private LocalDate checkoutDate;
	private Integer noOfPerson;
	
}
