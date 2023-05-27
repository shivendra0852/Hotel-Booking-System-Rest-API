package com.staywell.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.staywell.dto.RoomDTO;
import com.staywell.exception.HotelException;
import com.staywell.exception.RoomException;
import com.staywell.model.Hotel;
import com.staywell.model.Reservation;
import com.staywell.model.Room;
import com.staywell.repository.HotelDao;
import com.staywell.repository.ReservationDao;
import com.staywell.repository.RoomDao;

@Service
public class RoomServiceImpl implements RoomService{
	
	@Autowired
	private HotelDao hotelDao;
	
	@Autowired
	private RoomDao roomDao;
	
	@Autowired
	private ReservationDao reservationDao;
	
	@Override
	public Room addRoom(Room room) throws RoomException {
		
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		Hotel hotel = hotelDao.findByHotelEmail(email).get();
		
		List<Room> rooms = hotel.getRooms();
		for(Room r : rooms) {
			if(r.getRoomNumber() == room.getRoomNumber()) {
				throw new RoomException("Room already present in your hotel with room number : " + room.getRoomNumber());
			}
		}

		
		roomDao.save(room);
		
		hotel.getRooms().add(room);
		
		room.setHotel(hotel);
		
		hotelDao.save(hotel);
		
		return room;
		
	}

	@Override
	public Room updateRoom(Integer roomNo, RoomDTO roomDTO) throws RoomException {
		
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		Hotel hotel = hotelDao.findByHotelEmail(email).get();
		
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
		Hotel hotel = hotelDao.findByHotelEmail(email).get();
		
		Room room = null;
		
		/*Checking if provided room is exits or not*/
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
		
		List<Reservation> reservations = room.getReservations();
		
		/*Checking if there is any pending reservation of this room*/
		for(Reservation r : reservations) {
			if(!r.getStatus().toString().equals("CLOSED")) {
				room.setAvailable(false);
				roomDao.save(room);
				throw new RoomException("Booked Room can't be removed, but it is set to not available for future bookings");
			}
		}
		
		/*Removing reference of room from every reservation of this room
		 *  to avoid deleting reservation with room due to cascade ALL*/
		for(Reservation r : reservations) {
			r.setRoom(null);
			reservationDao.save(r);
		}
		
		/*Removing reference of room from hotel to avoid deleting hotel with room due to cascade ALL*/
		rooms.remove(room);
		hotel.setRooms(rooms);
		hotelDao.save(hotel);
		
		roomDao.delete(room);
		
		return "Room removed successfully";
		
	}

	@Override
	public List<Room> getAllAvailableRoomsByHotelId(Long hotelId, LocalDate checkIn, LocalDate checkOut) throws RoomException {
		
		Optional<Hotel> opt = hotelDao.findById(hotelId);
		if(opt.isEmpty()) {
			throw new HotelException("Hotel not found with id : " + hotelId);
		}
		
		Hotel  hotel = opt.get();
		
		/*Getting all rooms and filtering if available*/
		List<Room> rooms = hotel.getRooms().stream().filter(h -> h.getAvailable()).collect(Collectors.toList());
		
		/*Filtering out closed reservations out of all reservation of that hotel*/
		List<Reservation> reservations = hotel.getReservations().stream().filter((r) -> !r.getStatus().toString().equals("CLOSED")).collect(Collectors.toList());
		
		/*Filtering out rooms that are not available for the provided dates*/
		for(Reservation r : reservations) {
			for(Room room : rooms) {
				if((room.getRoomId()==r.getRoom().getRoomId()) &&
						(checkIn.isEqual(r.getCheckinDate()) || checkIn.isEqual(r.getCheckinDate())) ||
						(checkOut.isEqual(r.getCheckinDate()) || checkOut.isEqual(r.getCheckinDate())) ||
						(checkIn.isAfter(r.getCheckinDate()) && checkIn.isBefore(r.getCheckinDate())) ||
						(checkOut.isAfter(r.getCheckinDate()) && checkOut.isBefore(r.getCheckinDate()))
						) {
					rooms.remove(room);
				}
			}
		}
		
		if(rooms.isEmpty()) throw new RoomException("Rooms not found in this hotel");
		
		return rooms;
		
	}

}
