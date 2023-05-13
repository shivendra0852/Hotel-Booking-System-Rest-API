package com.staywell.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
	private String cardholderName;
    private String cardNumber;
    private String cvv;
    private LocalDateTime expirationDateTime;
}
