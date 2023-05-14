package com.staywell.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.staywell.model.Hotel;

public interface HotelDao extends JpaRepository<Hotel, Integer>{

	Optional<Hotel> findByHotelEmail(String email);

}
