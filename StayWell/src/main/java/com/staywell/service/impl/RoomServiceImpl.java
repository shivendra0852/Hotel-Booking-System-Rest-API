package com.staywell.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.staywell.dto.request.DateRequest;
import com.staywell.dto.request.RoomRequest;
import com.staywell.dto.request.UpdateRequest;
import com.staywell.dto.response.RoomResponse;
import com.staywell.enums.RoomType;
import com.staywell.exception.HotelException;
import com.staywell.exception.RoomException;
import com.staywell.model.Hotel;
import com.staywell.model.Reservation;
import com.staywell.model.Room;
import com.staywell.repository.HotelDao;
import com.staywell.repository.ReservationDao;
import com.staywell.repository.RoomDao;
import com.staywell.service.RoomService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
@Transactional
public class RoomServiceImpl implements RoomService {

	private PasswordEncoder passwordEncoder;
	private HotelDao hotelDao;
	private RoomDao roomDao;
	private ReservationDao reservationDao;

	@Override
	public RoomResponse addRoom(RoomRequest roomRequest) {
		Hotel currentHotel = getCurrentLoggedInHotel();

		log.info("Verifying credentials");
		String password = new String(roomRequest.getPassword());
		if (!passwordEncoder.matches(password, currentHotel.getPassword())) {
			throw new HotelException("Wrong credentials!");
		}

		List<Room> rooms = currentHotel.getRooms();
		log.info("Validating Room number");
		for (Room r : rooms) {
			if (r.getRoomNumber() == roomRequest.getRoomNumber()) {
				throw new RoomException(
						"Room already present in your hotel with room number : " + roomRequest.getRoomNumber());
			}
		}

		Room room = buildRoom(roomRequest);

		log.info("Assigning room to the Hotel : " + currentHotel.getName());
		currentHotel.getRooms().add(room);
		room.setHotel(currentHotel);

		roomDao.save(room);

		log.info("Room saved successfully");
		return buildRoomResponse(room);
	}

	@Override
	public String updateRoomType(UpdateRequest updateRequest, Long roomId) {
		Hotel hotel = getCurrentLoggedInHotel();

		log.info("Verifying credentials");
		if (!passwordEncoder.matches(new String(updateRequest.getPassword()), hotel.getPassword())) {
			throw new RoomException("Wrong credentials!");
		}
		roomDao.setRoomType(roomId, RoomType.valueOf(updateRequest.getField()));

		log.info("Updation successfull");
		return "Updated Room Type to " + updateRequest.getField();
	}

	@Override
	public String updateNoOfPerson(UpdateRequest updateRequest, Long roomId) {
		Hotel hotel = getCurrentLoggedInHotel();

		log.info("Verifying credentials");
		if (!passwordEncoder.matches(new String(updateRequest.getPassword()), hotel.getPassword())) {
			throw new RoomException("Wrong credentials!");
		}
		roomDao.setNoOfPerson(roomId, Integer.valueOf(updateRequest.getField()));

		log.info("Updation successfull");
		return "Updated number of person allowed per Room to " + updateRequest.getField();
	}

	@Override
	public String updatePrice(UpdateRequest updateRequest, Long roomId) {
		Hotel hotel = getCurrentLoggedInHotel();

		log.info("Verifying credentials");
		if (!passwordEncoder.matches(new String(updateRequest.getPassword()), hotel.getPassword())) {
			throw new RoomException("Wrong credentials!");
		}
		roomDao.setPrice(roomId, BigDecimal.valueOf(Double.valueOf(updateRequest.getField())));

		log.info("Updation successfull");
		return "Updated Room Price to " + updateRequest.getField();
	}

	@Override
	public String updateAvailable(UpdateRequest updateRequest, Long roomId) {
		Hotel hotel = getCurrentLoggedInHotel();

		log.info("Verifying credentials");
		if (!passwordEncoder.matches(new String(updateRequest.getPassword()), hotel.getPassword())) {
			throw new RoomException("Wrong credentials!");
		}
		roomDao.setAvailable(roomId, Boolean.valueOf(updateRequest.getField()));

		log.info("Updation successfull");
		return "Updated Room's availability to " + updateRequest.getField();
	}

	@Override
	public String removeRoom(UpdateRequest updateRequest) {
		Hotel hotel = getCurrentLoggedInHotel();

		log.info("Verifying credentials");
		if (!passwordEncoder.matches(new String(updateRequest.getPassword()), hotel.getPassword())) {
			throw new RoomException("Wrong credentials!");
		}

		Long roomId = Long.valueOf(updateRequest.getField());
		Optional<Room> optional = roomDao.findById(roomId);
		if (optional.isEmpty())
			throw new RoomException("Room not found with Id : " + roomId);
		Room room = optional.get();

		List<Room> rooms = hotel.getRooms();
		List<Reservation> reservations = room.getReservations();

		log.info("Checking if this Room has any pending Reservations");
		for (Reservation r : reservations) {
			if (!r.getStatus().toString().equals("CLOSED")) {

				log.info("Room has pending Reservations");
				roomDao.setAvailable(roomId, false);

				throw new RoomException(
						"Booked Room can't be removed, but it is set to not available for future bookings");
			}
		}

		log.info("Removing reference of Room from every Reservation");
		for (Reservation r : reservations) {
			r.setRoom(null);
		}

		log.info("Removing reference of Room from Hotel");
		rooms.remove(room);
		hotel.setRooms(rooms);

		log.info("Deletion in progress");
		roomDao.delete(room);

		log.info("Room removed successfully");
		return "Room removed successfully";
	}

	@Override
	public List<RoomResponse> getAllAvailableRoomsByHotelId(Long hotelId, DateRequest dateRequest) {

		Optional<Hotel> opt = hotelDao.findById(hotelId);
		if (opt.isEmpty())
			throw new HotelException("Hotel not found with id : " + hotelId);
		Hotel hotel = opt.get();

		List<Room> rooms = hotel.getRooms().stream().filter(h -> h.getAvailable()).collect(Collectors.toList());
		List<Reservation> reservations = reservationDao.getPendingReservationsOfHotel(hotel);
		
		LocalDate checkIn = dateRequest.getCheckIn();
		LocalDate checkOut = dateRequest.getCheckOut();
		for (Reservation r : reservations) {
			for (Room room : rooms) {
				if ((room.getRoomId() == r.getRoom().getRoomId())
						&& (checkIn.isEqual(r.getCheckinDate()) || checkIn.isEqual(r.getCheckinDate()))
						|| (checkOut.isEqual(r.getCheckinDate()) || checkOut.isEqual(r.getCheckinDate()))
						|| (checkIn.isAfter(r.getCheckinDate()) && checkIn.isBefore(r.getCheckinDate()))
						|| (checkOut.isAfter(r.getCheckinDate()) && checkOut.isBefore(r.getCheckinDate()))) {
					rooms.remove(room);
				}
			}
		}

		if (rooms.isEmpty())
			throw new RoomException("Rooms not found in this hotel");
		return rooms.stream().map(this::buildRoomResponse).collect(Collectors.toList());
	}

	@Override
	public List<RoomResponse> getAllRoomsByHotel() {
		Hotel hotel = getCurrentLoggedInHotel();
		List<Room> rooms = hotel.getRooms();
		if (rooms.isEmpty())
			throw new RoomException("No rooms found");
		return rooms.stream().map(this::buildRoomResponse).collect(Collectors.toList());
	}

	private Hotel getCurrentLoggedInHotel() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		return hotelDao.findByHotelEmail(email)
				.orElseThrow(() -> new HotelException("Failed to fetch the hotel with the email: " + email));
	}

	private Room buildRoom(RoomRequest roomRequest) {
		return Room.builder()
				.roomNumber(roomRequest.getRoomNumber())
				.roomType(roomRequest.getRoomType())
				.noOfPerson(roomRequest.getNoOfPerson())
				.price(roomRequest.getPrice())
				.available(roomRequest.getAvailable())
				.reservations(new ArrayList<>())
				.build();
	}

	private RoomResponse buildRoomResponse(Room room) {
		return RoomResponse.builder()
				.roomId(room.getRoomId())
				.roomNumber(room.getRoomNumber())
				.roomType(room.getRoomType())
				.noOfPerson(room.getNoOfPerson())
				.price(room.getPrice())
				.available(room.getAvailable())
				.build();
	}
}
