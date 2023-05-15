package com.staywell.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.staywell.enums.PaymentType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
//	private String cardholderName;
//    private String cardNumber;
//    private String cvv;
//    private LocalDate expirationDate;
	
	private PaymentType paymentType;
	private Boolean paymentStatus;

}
