package com.staywell.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Address {
	private String streetAddress;
    private String city;
    private String state;
    private String postalCode;
    private String country;
}
