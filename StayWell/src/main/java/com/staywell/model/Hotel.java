package com.staywell.model;

import java.util.List;

import com.staywell.enums.HotelType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Hotel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer hotelId;
	private String name;
	private String email;
	private String phone;
	private String telephone;
	private String password;
	private Address address;
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
