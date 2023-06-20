package com.staywell.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.times;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import com.staywell.dto.request.HotelRequest;
import com.staywell.dto.request.UpdateRequest;
import com.staywell.dto.response.HotelResponse;
import com.staywell.enums.HotelType;
import com.staywell.enums.Role;
import com.staywell.enums.RoomType;
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
import com.staywell.service.impl.HotelServiceImpl;

@SpringBootTest(classes = HotelServiceTest.class) /* It is for configuration of application context */
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

	private Hotel hotel;

	private HotelRequest hotelRequest;

	@BeforeEach
	public void addCommonDetails() {
		Address address = new Address("Near Hospital", "Jaipur", "RJ", "123456", "India");
		List<String> amenities = new ArrayList<>();
		List<Room> rooms = new ArrayList<>();
		List<Reservation> reservations = new ArrayList<>();
		List<Feedback> feedbacks = new ArrayList<>();
		rooms.add(new Room(1001L, 1, RoomType.AC, 2, BigDecimal.valueOf(5000.0), true, null, reservations));
		hotel = new Hotel(Long.valueOf(1), "MyHotel", "myhotel@gmail.com", "Pass@1234", "9999999999", "9000000000",
				HotelType.HOTEL, address, Role.ROLE_HOTEL, amenities, rooms, reservations, feedbacks);

		hotelRequest = new HotelRequest();
		hotelRequest.setHotelEmail("myhotel@gmail.com");
		hotelRequest.setAddress(hotel.getAddress());
		hotelRequest.setName("MyHotel");
		hotelRequest.setPassword(new char[] {'1','2','3','4'});
		hotelRequest.setHotelPhone("9999999999");
		hotelRequest.setHotelPhone("9000000000");
		hotelRequest.setHotelType(HotelType.HOTEL);
	}

	@Test
	@Order(1)
	public void testRegisterHotel() {
		Hotel dummyHotel = hotel;

		when(hotelDao.findByHotelEmail(anyString())).thenReturn(Optional.empty());
		when(customerDao.findByEmail(anyString())).thenReturn(Optional.empty());

		when(hotelDao.save(any())).thenReturn(dummyHotel);
		HotelResponse hotel = hotelService.registerHotel(hotelRequest);

		assertEquals(HotelType.HOTEL, hotel.getHotelType());

		verify(hotelDao, times(1)).findByHotelEmail(anyString());
		verify(customerDao, times(1)).findByEmail(anyString());
		verify(hotelDao, times(1)).save(any());
	}

	@Test
	@Order(2)
	public void testGetHotelById1() {
		when(hotelDao.findById(anyLong())).thenReturn(Optional.of(hotel));
		HotelResponse result = hotelService.getHotelById(1L);

		assertEquals(1L, result.getHotelId());
		assertEquals(HotelType.HOTEL, result.getHotelType());

		verify(hotelDao, atMostOnce()).findById(anyLong());
	}

	@Test
	@Order(3)
	public void testGetHotelById2() {
		when(hotelDao.findById(anyLong())).thenReturn(Optional.empty());
		assertThrows(HotelException.class, () -> hotelService.getHotelById(hotel.getHotelId()));
		verify(hotelDao, times(1)).findById(anyLong());
	}

	@Test
	@Order(4)
	public void testGetHotelsNearMe() {
		SecurityContextHolder.setContext(securityContext);
		when(securityContext.getAuthentication()).thenReturn(authentication);
		when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("customer@gmail.com");

		Customer customer = new Customer();
		customer.setAddress(new Address("Near Hospital", "Jaipur", "RJ", "123456", "India"));
		when(customerDao.findByEmail(anyString())).thenReturn(Optional.of(customer));
		when(hotelDao.getHotelByCity(anyString())).thenReturn(List.of(hotel));

		List<HotelResponse> hotels = hotelService.getHotelsNearMe();
		for (HotelResponse hotel : hotels) {
			assertEquals(customer.getAddress(), hotel.getAddress());
		}

		verify(customerDao, times(1)).findByEmail(anyString());
		verify(hotelDao, times(1)).getHotelByCity(anyString());
	}

	@Test
	@Order(5)
	public void testUpdatePhone() {
		SecurityContextHolder.setContext(securityContext);
		when(securityContext.getAuthentication()).thenReturn(authentication);
		when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("myhotel@gmail.com");

		when(hotelDao.findByHotelEmail(anyString())).thenReturn(Optional.of(hotel));
		when(hotelDao.setPhoneOfHotel(anyLong(), anyString())).thenReturn(1);

		UpdateRequest updateRequest = new UpdateRequest("9315807215", new char[] {'1','2','3','4'});
		when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

		String result = hotelService.updatePhone(updateRequest);
		assertEquals("Updated hotel phone successfully", result);

		verify(hotelDao, times(1)).setPhoneOfHotel(anyLong(), anyString());
		verify(hotelDao, times(1)).findByHotelEmail(anyString());
	}

	@Test
	@Order(6)
	public void testUpdateTelephone() {
		SecurityContextHolder.setContext(securityContext);
		when(securityContext.getAuthentication()).thenReturn(authentication);
		when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("myhotel@gmail.com");

		when(hotelDao.findByHotelEmail(anyString())).thenReturn(Optional.of(hotel));
		when(hotelDao.setTelephoneOfHotel(anyLong(), anyString())).thenReturn(1);

		UpdateRequest updateRequest = new UpdateRequest("1234567890", new char[] {'1','2','3','4'});
		when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

		String result = hotelService.updateTelephone(updateRequest);
		assertEquals("Updated hotel telephone successfully", result);

		verify(hotelDao, times(1)).setTelephoneOfHotel(1L, "1234567890");
		verify(hotelDao, times(1)).findByHotelEmail("myhotel@gmail.com");
	}

	@Test
	@Order(7)
	public void testUpdateName() {
		SecurityContextHolder.setContext(securityContext);
		when(securityContext.getAuthentication()).thenReturn(authentication);
		when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("myhotel@gmail.com");

		when(hotelDao.findByHotelEmail(anyString())).thenReturn(Optional.of(hotel));
		when(hotelDao.setNameOfHotel(anyLong(), anyString())).thenReturn(1);

		UpdateRequest updateRequest = new UpdateRequest("mynewhotel", new char[] {'1','2','3','4'});
		when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

		String result = hotelService.updateName(updateRequest);
		assertEquals("Updated hotel name successfully", result);

		verify(hotelDao, times(1)).setNameOfHotel(1L, "mynewhotel");
		verify(hotelDao, times(1)).findByHotelEmail("myhotel@gmail.com");
	}

	@Test
	@Order(8)
	public void testUpdateHotelType() {
		SecurityContextHolder.setContext(securityContext);
		when(securityContext.getAuthentication()).thenReturn(authentication);
		when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("myhotel@gmail.com");

		when(hotelDao.findByHotelEmail(anyString())).thenReturn(Optional.of(hotel));
		when(hotelDao.setHotelType(anyLong(), any())).thenReturn(1);

		UpdateRequest updateRequest = new UpdateRequest("HOTEL", new char[] {'1','2','3','4'});
		when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

		String result = hotelService.updateHotelType(updateRequest);
		assertEquals("Updated hotel type successfully", result);

		verify(hotelDao, times(1)).setHotelType(anyLong(), any());
		verify(hotelDao, times(1)).findByHotelEmail(anyString());
	}

	@Test
	@Order(9)
	public void testGetHotelsInCity() {
		Customer customer = new Customer();
		customer.setAddress(new Address("Near Hospital", "Jaipur", "RJ", "123456", "India"));

		when(hotelDao.getHotelByCity(any())).thenReturn(new ArrayList<>());

		assertThrows(HotelException.class, () -> hotelService.getHotelsInCity("Jaipur"));
	}

}
