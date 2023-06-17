package com.staywell.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.staywell.enums.Gender;
import com.staywell.model.Address;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {

	@NotNull @NotEmpty @NotBlank
    private String name;

	@NotNull @NotEmpty @NotBlank
    private String email;

	@NotNull @NotEmpty @NotBlank
    private String phone;

	@NotNull @NotEmpty @NotBlank
	@Enumerated(EnumType.STRING)
    private Gender gender;

	@NotNull @NotEmpty @NotBlank
	@JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dob;

	@NotNull @NotEmpty @NotBlank
	@Pattern(regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}")
    private String password;

	@Valid
    private Address address;

}
