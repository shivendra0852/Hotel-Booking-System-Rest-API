package com.staywell.model;

import java.util.ArrayList;
import java.util.List;

import com.staywell.enums.HotelType;

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
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Hotel {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long hotelId;
	@NotNull
	@NotEmpty
	private String name;
	@Column(unique = true)
	@Email
	@NotNull
	private String hotelEmail;
	@NotNull
	@NotEmpty
	private String hotelPhone;
	@NotNull
	@NotEmpty
	private String hotelTelephone;
	
	@Pattern(regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}")
	@NotNull
	@NotEmpty
	private String password;

	@Embedded
	private Address address;
	
	private String role;
	
	@Enumerated(EnumType.STRING)
	private HotelType hotelType;
	
	@ElementCollection(fetch = FetchType.EAGER)
	private List<String> amenities = new ArrayList<>();
	
	@OneToMany(mappedBy = "hotel", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<Room> rooms = new ArrayList<>();
	
	@OneToMany(mappedBy = "hotel", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<Reservation> reservations = new ArrayList<>();
	
	@OneToMany(mappedBy = "hotel", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<Feedback> feedbacks = new ArrayList<>();
	
}
