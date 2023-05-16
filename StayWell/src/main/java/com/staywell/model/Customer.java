package com.staywell.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.staywell.enums.Gender;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
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
public class Customer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
	@OneToMany(mappedBy = "customer")
	private List<Reservation> reservations;
	private LocalDateTime registrationDateTime;
}
