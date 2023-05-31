package com.staywell.model;

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

	@Enumerated(EnumType.STRING)
	private PaymentType paymentType;
	private String txnId;

}
