package com.staywell.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Address {

	@NotNull @NotEmpty @NotBlank
	private String streetAddress;
	@NotNull @NotEmpty @NotBlank @Size(min = 4)
    private String city;
	@NotNull @NotEmpty @NotBlank
    private String state;
	@NotNull @NotEmpty @NotBlank
    private String postalCode;
	@NotNull @NotEmpty @NotBlank
    private String country;

}
