package com.staywell.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.staywell.enums.Gender;
import com.staywell.enums.Role;

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
public class Customer {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY) @JsonProperty(access = Access.READ_ONLY)
	private Long customerId;

	private String name;

	@Column(unique = true) @Email
	private String email;

	@JsonProperty(access = Access.WRITE_ONLY)
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
	private Boolean toBeDeleted = false;

	@JsonIgnore
	@OneToMany(mappedBy = "customer", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<Reservation> reservations = new ArrayList<>();

}
