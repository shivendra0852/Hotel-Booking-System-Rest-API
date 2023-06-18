package com.staywell.model;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.staywell.enums.RoomType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Room {

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	@JsonProperty(access = Access.READ_ONLY)
	private Long roomId;

	private Integer roomNumber;

	@Enumerated(EnumType.STRING)
	private RoomType roomType;

	private Integer noOfPerson;

	private BigDecimal price;

	private Boolean available;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER)
	private Hotel hotel;

	@JsonIgnore
	@OneToMany(mappedBy = "room", fetch = FetchType.EAGER)
	private List<Reservation> reservations;

}
