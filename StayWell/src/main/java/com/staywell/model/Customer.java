package com.staywell.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.staywell.enums.Gender;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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
public class Customer {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer customerId;
	
	private String name;

	@Column(unique = true)
	private String email;

	private String phone;

	@Enumerated(EnumType.STRING)
	private Gender gender;

	private LocalDate dob;

	private String password;

	@Embedded
	private Address address;

	private String role;
	private LocalDateTime registrationDateTime;

	@JsonIgnore
	@OneToMany(mappedBy = "customer", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<Reservation> reservations = new ArrayList<>();

}
