package com.staywell.model;

import java.util.List;

import com.staywell.enums.HotelType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
	private Integer hotelId;
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
	@NotNull
	@NotEmpty
	private String password;
	@NotNull
	@NotEmpty
	private Address hotel_address;
	private String role;
	
	@Enumerated(EnumType.STRING)
	private HotelType hotelType;
	private List<String> amenities;
	
	@OneToMany(mappedBy = "hotel")
	private List<Room> rooms;
	
	@OneToMany(mappedBy = "hotel")
	private List<Reservation> reservations;
	
	@OneToMany(mappedBy = "hotel")
	private List<Feedback> feedbacks;

	
	
	
}
