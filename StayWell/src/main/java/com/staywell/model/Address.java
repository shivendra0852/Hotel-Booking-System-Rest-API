package com.staywell.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
	private String streetAddress;
    private String city;
    private String state;
    private String postalCode;
    private String country;
}
