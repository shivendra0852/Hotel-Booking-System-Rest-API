package com.staywell.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public class HotelException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    
	public HotelException(String message) {
		super(message);
	}
	
}
