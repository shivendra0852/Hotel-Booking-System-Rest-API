package com.staywell.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.staywell.enums.ReservationStatus;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Reservation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer reservationId;
	private LocalDate checkinDate;
	private LocalDate checkoutDate;
	private Integer noOfPerson;
	
	@Embedded
	private Payment payment;
	
	@Enumerated(EnumType.STRING)
	private ReservationStatus status;
	
	@ManyToOne
	private Room room;
	
	@JsonIgnore
	@ManyToOne
	private Customer customer;
}
