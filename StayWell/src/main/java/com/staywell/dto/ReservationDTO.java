package com.staywell.dto;

import java.time.LocalDate;

import com.staywell.enums.ReservationStatus;
import com.staywell.model.Customer;
import com.staywell.model.Payment;
import com.staywell.model.Reservation;
import com.staywell.model.Room;

import jakarta.persistence.Embedded;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
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
