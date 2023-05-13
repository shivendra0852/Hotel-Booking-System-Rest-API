package com.staywell.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDTO {
	
	private String roomType;
	private Integer noOfPerson;
	private BigDecimal price;
	private Boolean available;
	
}
