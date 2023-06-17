package com.staywell.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.staywell.dto.RoomDTO;
import com.staywell.dto.UpdateDetailsDTO;
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

@AllArgsConstructor
@Service
@Slf4j
public class RoomServiceImpl implements RoomService {

	private PasswordEncoder passwordEncoder;
	private HotelDao hotelDao;
	private RoomDao roomDao;
	private ReservationDao reservationDao;

	@Override
	public Room addRoom(RoomDTO roomDTO) throws RoomException {

		Hotel hotel = getCurrentLoggedInHotel();

		List<Room> rooms = hotel.getRooms();
		log.info("Validating Room number");
		for (Room r : rooms) {
			if (r.getRoomNumber() == roomDTO.getRoomNumber()) {
				throw new RoomException(
						"Room already present in your hotel with room number : " + roomDTO.getRoomNumber());
			}
		}

		Room room = buildRoom(roomDTO);
		room = roomDao.save(room);

		log.info("Assigning room to the Hotel : " + hotel.getName());
		hotel.getRooms().add(room);
		room.setHotel(hotel);
		hotelDao.save(hotel);

		log.info("Room saved successfully");
		return room;

	}

	@Override
	public String updateRoomType(UpdateDetailsDTO updateRequest, Integer roomId) {
		Hotel hotel = getCurrentLoggedInHotel();

		log.info("Verifying password for updation");
		if (!passwordEncoder.matches(updateRequest.getPassword(), hotel.getPassword())) {
			throw new HotelException("Wrong credentials!");
		}
		roomDao.setRoomType(roomId, RoomType.valueOf(updateRequest.getField()));

		log.info("Updated Room Type to " + updateRequest.getField());
		return "Updated Room Type to " + updateRequest.getField();
	}

	@Override
	public String updateNoOfPerson(UpdateDetailsDTO updateRequest, Integer roomId) {
		Hotel hotel = getCurrentLoggedInHotel();

		log.info("Verifying password for updation");
		if (!passwordEncoder.matches(updateRequest.getPassword(), hotel.getPassword())) {
			throw new HotelException("Wrong credentials!");
		}
		roomDao.setNoOfPerson(roomId, Integer.valueOf(updateRequest.getField()));

		log.info("Updated number of person allowed per Room to " + updateRequest.getField());
		return "Updated number of person allowed per Room to " + updateRequest.getField();
	}

	@Override
	public String updatePrice(UpdateDetailsDTO updateRequest, Integer roomId) {
		Hotel hotel = getCurrentLoggedInHotel();

		log.info("Verifying password for updation");
		if (!passwordEncoder.matches(updateRequest.getPassword(), hotel.getPassword())) {
			throw new HotelException("Wrong credentials!");
		}
		roomDao.setPrice(roomId, BigDecimal.valueOf(Long.valueOf(updateRequest.getField())));

		log.info("Updated Room Price to " + updateRequest.getField());
		return "Updated Room Price to " + updateRequest.getField();
	}

	@Override
	public String updateAvailable(UpdateDetailsDTO updateRequest, Integer roomId) {
		Hotel hotel = getCurrentLoggedInHotel();

		log.info("Verifying password for updation");
		if (!passwordEncoder.matches(updateRequest.getPassword(), hotel.getPassword())) {
			throw new HotelException("Wrong credentials!");
		}
		roomDao.setAvailable(roomId, Boolean.valueOf(updateRequest.getField()));

		log.info("Updated Room's availability to " + updateRequest.getField());
		return "Updated Room's availability to " + updateRequest.getField();
	}

	@Override
	public String removeRoom(Integer roomId) throws RoomException {

		Hotel hotel = getCurrentLoggedInHotel();
		Optional<Room> opt = roomDao.findById(roomId);

		if (opt.isEmpty())
			throw new RoomException("Room not found with Id : " + roomId);
		Room room = opt.get();

		List<Room> rooms = hotel.getRooms();
		List<Reservation> reservations = room.getReservations();

		/* Checking if there is any pending reservation of this room */
		log.info("Checking if this Room has any pending Reservations");
		for (Reservation r : reservations) {
			if (!r.getStatus().toString().equals("CLOSED")) {

				log.info("Room has pending Reservations");
				roomDao.setAvailable(roomId, false);

				throw new RoomException(
						"Booked Room can't be removed, but it is set to not available for future bookings");
			}
		}

		/*
		 * Removing reference of room from every reservation of this room to avoid
		 * deleting reservation with room due to cascade ALL
		 */
		log.info("Removing reference of Room from every Reservation");
		for (Reservation r : reservations) {
			r.setRoom(null);
			reservationDao.save(r);
		}

		/*
		 * Removing reference of room from hotel to avoid deleting hotel with room due
		 * to cascade ALL
		 */
		log.info("Removing reference of Room from Hotel");
		rooms.remove(room);
		hotel.setRooms(rooms);
		hotelDao.save(hotel);

		roomDao.delete(room);

		log.info("Room removed successfully");
		return "Room removed successfully";

	}

	@Override
	public List<Room> getAllAvailableRoomsByHotelId(Long hotelId, LocalDate checkIn, LocalDate checkOut)
			throws RoomException {

		Optional<Hotel> opt = hotelDao.findById(hotelId);
		if (opt.isEmpty())
			throw new HotelException("Hotel not found with id : " + hotelId);
		Hotel hotel = opt.get();

		/* Getting all rooms and filtering if available */
		List<Room> rooms = hotel.getRooms().stream().filter(h -> h.getAvailable()).collect(Collectors.toList());

		/*
		 * Fetching current and future reservations out of all reservation of a
		 * particular hotel
		 */
		List<Reservation> reservations = reservationDao.getPendingReservationsOfHotel(hotel);

		/* Filtering out rooms that are not available for the provided dates */
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

		return rooms;

	}

	@Override
	public List<Room> getAllRoomsByHotel() throws RoomException {

		Hotel hotel = getCurrentLoggedInHotel();

		List<Room> rooms = hotel.getRooms();
		if (rooms.isEmpty())
			throw new RoomException("No rooms found");

		return rooms;

	}

	private Hotel getCurrentLoggedInHotel() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		return hotelDao.findByHotelEmail(email)
				.orElseThrow(() -> new HotelException("Failed to fetch the hotel with the email: " + email));
	}

	private Room buildRoom(RoomDTO roomDTO) {
		return Room.builder()
				.roomNumber(roomDTO.getRoomNumber())
				.roomType(roomDTO.getRoomType())
				.noOfPerson(roomDTO.getNoOfPerson())
				.price(roomDTO.getPrice())
				.available(roomDTO.getAvailable())
				.build();
	}

}
