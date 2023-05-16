package com.staywell.dto;

import java.time.LocalDate;

import com.staywell.enums.Gender;
import com.staywell.model.Address;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
