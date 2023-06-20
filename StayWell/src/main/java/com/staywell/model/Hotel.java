package com.staywell.model;

import java.util.List;

import com.staywell.enums.HotelType;
import com.staywell.enums.Role;

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

	@Column(unique = true)
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
	private List<String> amenities;

	@OneToMany(mappedBy = "hotel", fetch = FetchType.EAGER)
	private List<Room> rooms;

	@OneToMany(mappedBy = "hotel", fetch = FetchType.EAGER)
	private List<Reservation> reservations;

	@OneToMany(mappedBy = "hotel", fetch = FetchType.EAGER)
	private List<Feedback> feedbacks;

}
