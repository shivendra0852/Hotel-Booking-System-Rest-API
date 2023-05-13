package com.staywell.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import com.staywell.dto.RoomDTO;
import com.staywell.exception.RoomException;
import com.staywell.model.Hotel;
import com.staywell.model.Reservation;
import com.staywell.model.Room;
import com.staywell.repository.CustomerDao;
import com.staywell.repository.HotelDao;
import com.staywell.repository.RoomDao;

public class RoomServiceImpl implements RoomService{

	@Autowired
	private CustomerDao customerDao;
	
	@Autowired
	private HotelDao hotelDao;
	
	@Autowired
	private RoomDao roomDao;
	
	@Override
	public Room addRoom(Room room) throws RoomException {
		
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		Hotel hotel = hotelDao.findByEmail(email).get();
		
		List<Room> rooms = hotel.getRooms();
		for(Room r : rooms) {
			if(r.getRoomNumber() == room.getRoomNumber()) {
				throw new RoomException("Room already present in your hotel with room number : " + room.getRoomNumber());
			}
		}
		
		rooms.add(room);
		room.setHotel(hotel);
		
		return roomDao.save(room);
		
	}

	@Override
	public Room updateRoom(Integer roomNo, RoomDTO roomDTO) throws RoomException {
		
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		Hotel hotel = hotelDao.findByEmail(email).get();
		
		Room room = null;
		List<Room> rooms = hotel.getRooms();
		for(Room r : rooms) {
			if(r.getRoomNumber() == roomNo) {
				room = r;
				break;
			}
		}
		
		if(room == null) {
			throw new RoomException("Room not found in your hotel with room number : "+ roomNo);
		}
		
		if(roomDTO.getNoOfPerson() != null) {
			room.setNoOfPerson(roomDTO.getNoOfPerson());
		}
		if(roomDTO.getPrice() != null) {
			room.setPrice(roomDTO.getPrice());
		}
		if(roomDTO.getRoomType() != null) {
			room.setRoomType(roomDTO.getRoomType());
		}
		if(roomDTO.getAvailable() != room.getAvailable() && roomDTO.getAvailable() != null) {
			room.setAvailable(roomDTO.getAvailable());
		}
		
		return roomDao.save(room);
		
	}

	@Override
	public String removeRoom(Integer roomNo) throws RoomException{
		
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		Hotel hotel = hotelDao.findByEmail(email).get();
		
		Room room = null;
		List<Room> rooms = hotel.getRooms();
		for(Room r : rooms) {
			if(r.getRoomNumber() == roomNo) {
				room = r;
				break;
			}
		}
		
		if(room == null) {
			throw new RoomException("Room not found in your hotel with room number : "+ roomNo);
		}
		
		List<Reservation> reservations = hotel.getReservations();
		for(Reservation r : reservations) {
			if(r.getRoom().getRoomNumber() == roomNo) {
				room.setAvailable(false);
				roomDao.save(room);
				throw new RoomException("Booked Room can't be removed, but it is set to not available for future bookings");
			}
		}
		
		roomDao.delete(room);
		
		return "Room removed successfully";
		
	}

	@Override
	public List<Room> getAllAvailableRoomsByHotelId(Integer hotelId, LocalDateTime checkIn, LocalDateTime checkOut) {
		
		return null;
		
	}

	@Override
	public List<Room> getAvailableRoomsNearMe(LocalDateTime checkIn, LocalDateTime checkOut) {
		
		return null;
		
	}

	@Override
	public List<Room> getAvailableRoomsInCity(String city, LocalDateTime checkIn, LocalDateTime checkOut) {
		
		return null;
		
	}

}
