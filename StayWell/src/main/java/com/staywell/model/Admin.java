package com.staywell.model;



import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.staywell.enums.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Admin {

	@Id @GeneratedValue(strategy = GenerationType.AUTO) @JsonProperty(access = Access.READ_ONLY)
	private Integer adminId;

	@NotNull @NotEmpty @NotBlank
	private String name;

	@NotNull @NotEmpty @NotBlank
	@Column(unique = true) @Email
	private String email;

	@NotNull @NotEmpty @NotBlank
	private String mobile;

	@NotNull @NotEmpty @NotBlank
	@JsonProperty(access = Access.WRITE_ONLY)
	private String password;

	@JsonIgnore
	@Enumerated(EnumType.STRING)
	private Role role;
	
}
