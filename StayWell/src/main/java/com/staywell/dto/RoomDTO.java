package com.staywell.dto;

import java.math.BigDecimal;

import com.staywell.enums.RoomType;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDTO {
	
	@Min(1) @NotBlank @NotNull @NotEmpty
	private Integer roomNumber;
	
	@NotBlank @NotNull @NotEmpty
	@Enumerated(EnumType.STRING)
	private RoomType roomType;
	
	@Min(1) @NotBlank @NotNull @NotEmpty
	private Integer noOfPerson;
	
	@Min(100) @NotBlank @NotNull @NotEmpty
	private BigDecimal price;
	
	@NotBlank @NotNull @NotEmpty
	private Boolean available;
	
}
