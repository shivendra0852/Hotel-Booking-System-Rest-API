package com.staywell.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.staywell.enums.ReservationStatus;

import jakarta.persistence.CascadeType;
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
	
	@Id @GeneratedValue(strategy = GenerationType.AUTO) @JsonProperty(access = Access.READ_ONLY)
	private Integer reservationId;
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate checkinDate;
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate checkoutDate;
	
	private Integer noOfPerson;
	
	@Embedded
	private Payment payment;
	
	@Enumerated(EnumType.STRING)
	private ReservationStatus status;
	
	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Room room;
	
	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Hotel hotel;
	
	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Customer customer;
	
}
