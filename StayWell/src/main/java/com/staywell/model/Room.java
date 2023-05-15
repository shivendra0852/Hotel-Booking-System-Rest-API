package com.staywell.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Room {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonProperty(access = Access.READ_ONLY)
	private Integer roomId;
	
	private Integer roomNumber;
	private String roomType;
	private Integer noOfPerson;
	private BigDecimal price;
	private Boolean available;
	
	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL)
	private Hotel hotel;
	
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "room")
	private List<Reservation> reservations = new ArrayList<>();
	
}
