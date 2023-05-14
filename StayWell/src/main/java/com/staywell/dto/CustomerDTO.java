package com.staywell.dto;

import com.staywell.enums.Gender;
import com.staywell.model.Address;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {

    private String name;

    private String email;

    private String phone;

    private Gender gender;

    private LocalDate dob;

    private String password;

    private Address address;

}
