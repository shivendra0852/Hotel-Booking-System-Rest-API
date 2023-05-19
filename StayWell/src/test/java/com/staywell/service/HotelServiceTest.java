package com.staywell.service;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import org.springframework.security.crypto.password.PasswordEncoder;

import com.staywell.dto.HotelDTO;
import com.staywell.dto.UpdateHotelDetailsDTO;
import com.staywell.enums.HotelType;
import com.staywell.exception.HotelException;
import com.staywell.model.Address;
import com.staywell.model.Customer;
import com.staywell.model.Feedback;
import com.staywell.model.Hotel;
import com.staywell.model.Reservation;
import com.staywell.model.Room;
import com.staywell.repository.CustomerDao;
import com.staywell.repository.HotelDao;
import com.staywell.repository.ReservationDao;

@SpringBootTest(classes = HotelServiceTest.class) /*It is for configuration of application context*/
@ExtendWith(MockitoExtension.class) /**/
@TestMethodOrder(OrderAnnotation.class)
public class HotelServiceTest {
	
	@Mock
	private HotelDao hotelDao;
	
	@Mock
	private CustomerDao customerDao;
	
	@Mock
	private ReservationDao reservationDao;
	
	@Mock
	private SecurityContext securityContext;
	
	@Mock
	private Authentication authentication;
	
	@Mock
	private PasswordEncoder passwordEncoder;
	
	@InjectMocks
	private HotelServiceImpl hotelService;
	
	
	
	@Test
	@Order(1)
	public void testRegisterHotel() {
		
		
		Address address = new Address("Near Hospital", "Jaipur", "RJ", "123456", "India");
		List<String> amenities = new ArrayList<>();
		List<Room> rooms = new ArrayList<>();
		List<Reservation> reservations = new ArrayList<>();
		List<Feedback> feedbacks = new ArrayList<>();
		rooms.add(new Room(1001, 1, "AC", 2, BigDecimal.valueOf(5000.0), true, null, reservations));

		Hotel dummyHotel = new Hotel(Long.valueOf(1), "MyHotel", "myhotel@gmail.com", "9999999999", "9000000000", "1234", address, "HOTEL",
				HotelType.valueOf("Hotel"), amenities, rooms, reservations, feedbacks);
		
		when(hotelDao.save(any())).thenReturn(dummyHotel);
		
		when(customerDao.findByEmail(anyString())).thenReturn(Optional.empty());
		doReturn(Optional.empty()).when(hotelDao).findByHotelEmail(anyString());
		
		HotelDTO hotelDTO = new HotelDTO();
		hotelDTO.setEmail("any@gmail.com");
		
		Hotel hotel = hotelService.registerHotel(hotelDTO);
		
		assertEquals("MyHotel", hotel.getName());
		
	}

	@Test
	@Order(2)
	public void testGetHotelById() {
		Address address = new Address("Near Hospital", "Jaipur", "RJ", "123456", "India");
		List<String> amenities = new ArrayList<>();
		List<Room> rooms = new ArrayList<>();
		List<Reservation> reservations = new ArrayList<>();
		List<Feedback> feedbacks = new ArrayList<>();
		rooms.add(new Room(1001, 1, "AC", 2, BigDecimal.valueOf(5000.0), true, null, reservations));

		Hotel dummyHotel = new Hotel(Long.valueOf(1), "MyHotel", "myhotel@gmail.com", "9999999999", "9000000000", "1234", address, "HOTEL",
				HotelType.valueOf("Hotel"), amenities, rooms, reservations, feedbacks);
		
		when(hotelDao.findById(anyLong())).thenReturn(Optional.of(dummyHotel));
		
		Hotel hotel = hotelService.getHotelById(1L);
		
		assertEquals(1L, hotel.getHotelId());
		assertEquals("myhotel@gmail.com", hotel.getHotelEmail());
		
	}
	
	@Test
	@Order(3)
	public void testUpdateEmail() {
		
		Address address = new Address("Near Hospital", "Jaipur", "RJ", "123456", "India");
		List<String> amenities = new ArrayList<>();
		List<Room> rooms = new ArrayList<>();
		List<Reservation> reservations = new ArrayList<>();
		List<Feedback> feedbacks = new ArrayList<>();
		rooms.add(new Room(1001, 1, "AC", 2, BigDecimal.valueOf(5000.0), true, null, reservations));

		Hotel dummyHotel = new Hotel(Long.valueOf(1), "MyHotel", "myhotel@gmail.com", "9999999999", "9000000000", "1234", address, "HOTEL",
				HotelType.valueOf("Hotel"), amenities, rooms, reservations, feedbacks);
		
		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("abc@gmailcom");

		when(hotelDao.findByHotelEmail(anyString())).thenReturn(Optional.of(dummyHotel));
		when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
		
		UpdateHotelDetailsDTO updateDTO = new UpdateHotelDetailsDTO();
		updateDTO.setPassword("1234");
		updateDTO.setField("hotel@gmail.com");
		
		when(hotelDao.setEmailOfHotel(anyLong(), anyString())).thenReturn(1);
		
		Hotel dummyHotel2 = dummyHotel;
		dummyHotel.setHotelEmail("hotel@gmail.com");
		
		when(hotelDao.findById(anyLong())).thenReturn(Optional.of(dummyHotel2));
		
		Hotel hotel = hotelService.updateEmail(updateDTO);
		
		assertEquals("hotel@gmail.com", hotel.getHotelEmail());
		assertEquals("9999999999", hotel.getHotelPhone());
		
	}
	
	@Test
	@Order(4)
	public void testUpdateTeleohone() {
		
		Address address = new Address("Near Hospital", "Jaipur", "RJ", "123456", "India");
		List<String> amenities = new ArrayList<>();
		List<Room> rooms = new ArrayList<>();
		List<Reservation> reservations = new ArrayList<>();
		List<Feedback> feedbacks = new ArrayList<>();
		rooms.add(new Room(1001, 1, "AC", 2, BigDecimal.valueOf(5000.0), true, null, reservations));

		Hotel dummyHotel = new Hotel(Long.valueOf(1), "MyHotel", "myhotel@gmail.com", "9999999999", "9000000000", "1234", address, "HOTEL",
				HotelType.valueOf("Hotel"), amenities, rooms, reservations, feedbacks);
		
		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("abc@gmailcom");

		when(hotelDao.findByHotelEmail(anyString())).thenReturn(Optional.of(dummyHotel));
		when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
		
		UpdateHotelDetailsDTO updateDTO = new UpdateHotelDetailsDTO();
		updateDTO.setPassword("1234");
		updateDTO.setField("8000000000");
		
		when(hotelDao.setEmailOfHotel(anyLong(), anyString())).thenReturn(1);
		
		Hotel dummyHotel2 = dummyHotel;
		dummyHotel.setHotelTelephone("8000000000");
		
		when(hotelDao.findById(anyLong())).thenReturn(Optional.of(dummyHotel2));
		
		Hotel hotel = hotelService.updateEmail(updateDTO);
		
		assertEquals("myhotel@gmail.com", hotel.getHotelEmail());
		assertEquals("8000000000", hotel.getHotelTelephone());
		
	}
	
	@Test
	@Order(5)
	public void testUpdatePhone() {
		
		Address address = new Address("Near Hospital", "Jaipur", "RJ", "123456", "India");
		List<String> amenities = new ArrayList<>();
		List<Room> rooms = new ArrayList<>();
		List<Reservation> reservations = new ArrayList<>();
		List<Feedback> feedbacks = new ArrayList<>();
		rooms.add(new Room(1001, 1, "AC", 2, BigDecimal.valueOf(5000.0), true, null, reservations));

		Hotel dummyHotel = new Hotel(Long.valueOf(1), "MyHotel", "myhotel@gmail.com", "9999999999", "9000000000", "1234", address, "HOTEL",
				HotelType.valueOf("Hotel"), amenities, rooms, reservations, feedbacks);
		
		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("abc@gmailcom");

		when(hotelDao.findByHotelEmail(anyString())).thenReturn(Optional.of(dummyHotel));
		when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
		
		UpdateHotelDetailsDTO updateDTO = new UpdateHotelDetailsDTO();
		updateDTO.setPassword("1234");
		updateDTO.setField("8888888888");
		
		when(hotelDao.setEmailOfHotel(anyLong(), anyString())).thenReturn(1);
		
		Hotel dummyHotel2 = dummyHotel;
		dummyHotel.setHotelPhone("8888888888");
		
		when(hotelDao.findById(anyLong())).thenReturn(Optional.of(dummyHotel2));
		
		Hotel hotel = hotelService.updateEmail(updateDTO);
		
		assertEquals("myhotel@gmail.com", hotel.getHotelEmail());
		assertEquals("8888888888", hotel.getHotelPhone());
		
	}
	
	@Test
	@Order(6)
	public void testUpdateName() {
		
		Address address = new Address("Near Hospital", "Jaipur", "RJ", "123456", "India");
		List<String> amenities = new ArrayList<>();
		List<Room> rooms = new ArrayList<>();
		List<Reservation> reservations = new ArrayList<>();
		List<Feedback> feedbacks = new ArrayList<>();
		rooms.add(new Room(1001, 1, "AC", 2, BigDecimal.valueOf(5000.0), true, null, reservations));

		Hotel dummyHotel = new Hotel(Long.valueOf(1), "MyHotel", "myhotel@gmail.com", "9999999999", "9000000000", "1234", address, "HOTEL",
				HotelType.valueOf("Hotel"), amenities, rooms, reservations, feedbacks);
		
		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("abc@gmailcom");

		when(hotelDao.findByHotelEmail(anyString())).thenReturn(Optional.of(dummyHotel));
		when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
		
		UpdateHotelDetailsDTO updateDTO = new UpdateHotelDetailsDTO();
		updateDTO.setPassword("1234");
		updateDTO.setField("StarHotel");
		
		when(hotelDao.setEmailOfHotel(anyLong(), anyString())).thenReturn(1);
		
		Hotel dummyHotel2 = dummyHotel;
		dummyHotel.setName("StarHotel");
		
		when(hotelDao.findById(anyLong())).thenReturn(Optional.of(dummyHotel2));
		
		Hotel hotel = hotelService.updateEmail(updateDTO);
		
		assertEquals("myhotel@gmail.com", hotel.getHotelEmail());
		assertEquals("StarHotel", hotel.getName());
		
	}

	@Test
	@Order(7)
	public void testGetHotelsNearMe() {
		
		List<Hotel> hotels = new ArrayList<>();
		hotels.add(new Hotel());
		
		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("abc@gmailcom");

		Customer customer = new Customer();
		customer.setAddress(new Address());
		
		when(customerDao.findByEmail(anyString())).thenReturn(Optional.of(customer));
		
		when(hotelDao.findByAddress(any())).thenReturn(hotels);
		
		List<Hotel> resultHotels = hotelService.getHotelsNearMe();
		
		assertEquals(1, resultHotels.size());
		
	}
	
	@Test
	@Order(8)
	public void testGetHotelsInCity() {
		
		Address address = new Address("Near Hospital", "Jaipur", "RJ", "123456", "India");

		Customer customer = new Customer();
		customer.setAddress(address);
				
		when(hotelDao.findByAddress(any())).thenReturn(new ArrayList<>());
		
		assertThrows(HotelException.class, ()-> hotelService.getHotelsInCity("Jaipur"));
		
	}
	
}
