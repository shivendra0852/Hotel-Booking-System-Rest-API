package com.staywell.model;

import java.time.LocalDate;

import com.staywell.dto.Payment;
import com.staywell.enums.ReservationStatus;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Reservation {

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Long reservationId;

	private LocalDate checkinDate;

	private LocalDate checkoutDate;

	private Integer noOfPerson;

	@Embedded
	private Payment payment;

	@Enumerated(EnumType.STRING)
	private ReservationStatus status;

	@ManyToOne(fetch = FetchType.EAGER)
	private Room room;

	@ManyToOne(fetch = FetchType.EAGER)
	private Hotel hotel;

	@ManyToOne(fetch = FetchType.EAGER)
	private Customer customer;

}
