package com.staywell.dto.request;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.staywell.enums.Gender;
import com.staywell.model.Address;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRequest {

	@NotNull @NotEmpty @NotBlank
    private String name;

	@NotNull @NotEmpty @NotBlank @Email
    private String email;

	@NotNull @Pattern(regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}")
    private char[] password;

	@NotNull @NotEmpty @NotBlank @Size(min = 10)
    private String phone;

	@NotNull
	@Enumerated(EnumType.STRING)
    private Gender gender;

	@NotNull
	@JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dob;

	@Valid
    private Address address;

}
