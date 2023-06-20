package com.staywell.dto;

import com.staywell.enums.PaymentType;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

	@NotNull
	@Enumerated(EnumType.STRING)
	private PaymentType paymentType;

	@NotNull @NotEmpty @NotBlank
	private String txnId;

}
