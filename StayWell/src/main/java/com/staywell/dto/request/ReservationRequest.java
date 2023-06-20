package com.staywell.dto.request;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequest {

	@JsonFormat(pattern = "dd-MM-yyyy")
	@FutureOrPresent
	private LocalDate checkinDate;

	@JsonFormat(pattern = "dd-MM-yyyy")
	@FutureOrPresent
	private LocalDate checkoutDate;

	@Min(1)
	private Integer noOfPerson;

}
