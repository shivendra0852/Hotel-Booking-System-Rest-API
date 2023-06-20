package com.staywell.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.staywell.enums.Gender;
import com.staywell.enums.Role;

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
	private Long customerId;

	private String name;

	@Column(unique = true)
	private String email;

	private String password;

	private String phone;

	@Enumerated(EnumType.STRING)
	private Gender gender;

	private LocalDate dob;

	@Embedded
	private Address address;

	@Enumerated(EnumType.STRING)
	private Role role;
	private LocalDateTime registrationDateTime;
	private boolean toBeDeleted;
	private LocalDateTime deletionScheduledAt;

	@OneToMany(mappedBy = "customer", fetch = FetchType.EAGER)
	private List<Reservation> reservations;

}
