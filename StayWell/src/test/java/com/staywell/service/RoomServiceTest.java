package com.staywell.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.staywell.dto.request.RoomRequest;
import com.staywell.dto.request.UpdateRequest;
import com.staywell.dto.response.RoomResponse;
import com.staywell.enums.HotelType;
import com.staywell.enums.Role;
import com.staywell.enums.RoomType;
import com.staywell.exception.RoomException;
import com.staywell.model.Address;
import com.staywell.model.Feedback;
import com.staywell.model.Hotel;
import com.staywell.model.Reservation;
import com.staywell.model.Room;
import com.staywell.repository.HotelDao;
import com.staywell.repository.ReservationDao;
import com.staywell.repository.RoomDao;
import com.staywell.service.impl.RoomServiceImpl;

@SpringBootTest(classes = RoomServiceTest.class)
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(OrderAnnotation.class)
public class RoomServiceTest {

	@Mock
	private SecurityContext securityContext;

	@Mock
	private Authentication authentication;

	@Mock
	private RoomDao roomDao;

	@Mock
	private HotelDao hotelDao;

	@Mock
	private ReservationDao reservationDao;

	@InjectMocks
	private RoomServiceImpl roomService;

	private Address address;
	private List<String> amenities;
	private List<Room> rooms;
	private List<Reservation> reservations;
	private List<Feedback> feedbacks;
	private Hotel hotel;

	@BeforeEach
	public void init() {
		address = new Address("Near Hospital", "Jaipur", "RJ", "123456", "India");
		amenities = new ArrayList<>();
		rooms = new ArrayList<>();
		reservations = new ArrayList<>();
		feedbacks = new ArrayList<>();
		rooms.add(new Room(1001L, 1, RoomType.AC, 2, BigDecimal.valueOf(5000.0), true, null, reservations));

		hotel = new Hotel(Long.valueOf(1), "MyHotel", "myhotel@gmail.com", "Pass@123", "9999999999", "9000000000",
				HotelType.HOTEL, address, Role.ROLE_HOTEL, amenities, rooms, reservations, feedbacks);
	}

	@Test
	@Order(1)
	public void testAddRoom() throws RoomException {

		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("myhotel@gmailcom");

		Room room = new Room(1002L, 2, RoomType.AC, 1, BigDecimal.valueOf(2000.0), true, hotel, reservations);

		RoomRequest roomRequest = new RoomRequest(new char[] {'1','2','3','4'}, 2, RoomType.AC, 1, BigDecimal.valueOf(2000.0), true);

		when(hotelDao.findByHotelEmail(anyString())).thenReturn(Optional.of(hotel));

		when(roomDao.save(any())).thenReturn(room);

		RoomResponse myRoom = roomService.addRoom(roomRequest);

		assertAll(() -> {
			assertNotNull(room);
			assertEquals(1002, myRoom.getRoomId());
			assertNotEquals(1, myRoom.getRoomNumber());
			assertTrue(myRoom.getAvailable());
		});

		verify(hotelDao).findByHotelEmail(anyString());

		verify(roomDao).save(any());

	}

	@Test
	@Order(3)
	public void testRemoveRoom() throws RoomException {

		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("abc@gmailcom");

		when(hotelDao.findByHotelEmail(anyString())).thenReturn(Optional.of(hotel));

		when(hotelDao.save(any())).thenReturn(hotel);

		Room room = new Room(1001L, 1, RoomType.AC, 2, BigDecimal.valueOf(5000.0), true, null, reservations);
		when(roomDao.findById(anyLong())).thenReturn(Optional.of(room));

		assertEquals(1, hotel.getRooms().size());

		doNothing().when(roomDao).delete(any());

		UpdateRequest updateRequest = new UpdateRequest("1001", new char[] {'1','2','3','4'});
		String result = roomService.removeRoom(updateRequest);

		assertAll(() -> {
			assertEquals("Room removed successfully", result);
			assertEquals(0, hotel.getRooms().size());
		});

		verify(roomDao, never()).save(any());
		verify(hotelDao).findByHotelEmail(anyString());
		verify(reservationDao, never()).save(any());
		verify(hotelDao).save(any());

		verify(roomDao).delete(any());

	}

	@Test
	@Order(4)
	public void testGetAllAvailableRoomsByHotelId() throws RoomException {

		when(hotelDao.findById(anyLong())).thenReturn(Optional.of(hotel));

		List<RoomResponse> resultRooms = roomService.getAllAvailableRoomsByHotelId(Long.valueOf(1), null);

		assertAll(() -> {
			assertEquals(1, resultRooms.size());
			assertEquals(1001, resultRooms.get(0).getRoomId());
		});

		verify(hotelDao).findById(anyLong());

	}

}
