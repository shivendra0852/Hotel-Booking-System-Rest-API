package com.staywell.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.staywell.enums.HotelType;
import com.staywell.model.Hotel;

public interface HotelDao extends JpaRepository<Hotel, Long> {

	Optional<Hotel> findByHotelEmail(String email);

	@Modifying
	@Query("update Hotel set name=?2 where hotelId=?1")
	Integer setNameOfHotel(Long hotelId, String name);

	@Modifying
	@Query("update Hotel set password=?2 where hotelId=?1")
	void setHotelPassword(Long hotelId, String password);

	@Modifying
	@Query("update Hotel set hotelPhone=?2 where hotelId=?1")
	Integer setPhoneOfHotel(Long hotelId, String phone);

	@Modifying
	@Query("update Hotel set hotelTelephone=?2 where hotelId=?1")
	Integer setTelephoneOfHotel(Long hotelId, String telephone);

	@Modifying
	@Query("update Hotel set hotelType=?2 where hotelId=?1")
	Integer setHotelType(Long hotelId, HotelType hotelType);

	@Query("select h from Hotel h where h.address.city=?1")
	List<Hotel> getHotelByCity(String city);

	@Query("select h from Hotel h where h.name=?1 and h.address.city=?2")
	Optional<Hotel> getHotelByNameAndCity(String name, String city);

}
