package com.staywell.model;

import java.util.Objects;

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
	@Size(min=8, message="Please enter a valid streetAdress")
	private String streetAddress;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    
	@Override
	public int hashCode() {
		return Objects.hash(city);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Address other = (Address) obj;
		return Objects.equals(city, other.city);
	}
    
}
