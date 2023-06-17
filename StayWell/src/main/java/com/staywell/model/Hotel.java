package com.staywell.model;

import java.util.ArrayList;
import java.util.List;

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

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Long hotelId;

	private String name;

	@Column(unique = true) @Email
	private String hotelEmail;

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
	private List<String> amenities = new ArrayList<>();

	@OneToMany(mappedBy = "hotel", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<Room> rooms = new ArrayList<>();

	@OneToMany(mappedBy = "hotel", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<Reservation> reservations = new ArrayList<>();

	@OneToMany(mappedBy = "hotel", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<Feedback> feedbacks = new ArrayList<>();

}
