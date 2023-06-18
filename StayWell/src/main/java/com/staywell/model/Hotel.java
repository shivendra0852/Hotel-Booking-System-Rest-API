package com.staywell.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.staywell.enums.HotelType;
import com.staywell.enums.Role;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Hotel {

	@Id @GeneratedValue(strategy = GenerationType.AUTO)	@JsonProperty(access = Access.READ_ONLY)
	private Long hotelId;

	private String name;

	@Column(unique = true) @Email
	private String hotelEmail;

	@JsonProperty(access = Access.WRITE_ONLY)
	private String password;

	private String hotelPhone;

	private String hotelTelephone;

	@Enumerated(EnumType.STRING)
	private HotelType hotelType;

	@Embedded
	private Address address;

	@Enumerated(EnumType.STRING)
	private Role role;

	@ElementCollection(fetch = FetchType.EAGER)
	private List<String> amenities;

	@JsonIgnore
	@OneToMany(mappedBy = "hotel", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<Room> rooms;

	@JsonIgnore
	@OneToMany(mappedBy = "hotel", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<Reservation> reservations;

	@JsonIgnore
	@OneToMany(mappedBy = "hotel", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<Feedback> feedbacks;

}
