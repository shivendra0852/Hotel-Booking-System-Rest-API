package com.staywell.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.staywell.enums.PaymentType;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
	
	@Enumerated(EnumType.STRING)
	private PaymentType paymentType;
	private Boolean paymentStatus;

}
