package com.staywell.exception;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyErrorDetails {

	private LocalDateTime timestamp;
	private String message;
	private String details;

}
