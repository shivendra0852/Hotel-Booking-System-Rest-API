package com.staywell.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Feedback {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY) @JsonProperty(access = Access.READ_ONLY)
	private Integer feedbackId;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Hotel hotel;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Customer customer;

	private String feedbackContent;
	private Integer rating;

}
