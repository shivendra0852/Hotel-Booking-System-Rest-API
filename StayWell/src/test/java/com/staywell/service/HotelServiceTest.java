package com.staywell.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.staywell.dto.HotelDTO;
import com.staywell.dto.UpdateHotelDetailsDTO;
import com.staywell.enums.Gender;
import com.staywell.enums.HotelType;
import com.staywell.enums.Role;
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
	
	private Hotel hotel;
	
	private HotelDTO hotelRequest;
	@BeforeEach
	public void addCommonDetails() {
		
		Address address = new Address("Near Hospital", "Jaipur", "RJ", "123456", "India");
		List<String> amenities = new ArrayList<>();
		List<Room> rooms = new ArrayList<>();
		List<Reservation> reservations = new ArrayList<>();
		List<Feedback> feedbacks = new ArrayList<>();
		rooms.add(new Room(1001, 1, "AC", 2, BigDecimal.valueOf(5000.0), true, null, reservations));
		hotel = new Hotel(Long.valueOf(1), "MyHotel", "myhotel@gmail.com", "9999999999", "9000000000", new BCryptPasswordEncoder().encode("1234"), address, "HOTEL",
				HotelType.valueOf("Hotel"), amenities, rooms, reservations, feedbacks);
		
		 hotelRequest = new HotelDTO();
		hotelRequest.setHotelEmail("myhotel@gmail.com");
		hotelRequest.setAddress(hotel.getAddress());
		hotelRequest.setName("MyHotel");
		hotelRequest.setPassword("1234");
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
		Hotel hotel = hotelService.registerHotel(hotelRequest);
		
		assertEquals("myhotel@gmail.com", hotel.getHotelEmail());
		
		verify(hotelDao, times(1)).findByHotelEmail(anyString());
		verify(customerDao, times(1)).findByEmail(anyString());
		verify(hotelDao, times(1)).save(any());
	}
	
	
	@Test
    @Order(2)
	public void testGetHotelByIdIfHotelExists() {
		when(hotelDao.findById(anyLong())).thenReturn(Optional.of(hotel));
		Hotel resultantHotel = hotelService.getHotelById(hotel.getHotelId());
		assertEquals(1L, resultantHotel.getHotelId());
		verify(hotelDao, times(1)).findById(anyLong());
	}
	
	@Test
    @Order(3)
	public void testGetHotelByIdIfHotelNotExist() {
		when(hotelDao.findById(anyLong())).thenReturn(Optional.empty());
	     /*Checking the exception class is same that is thrown by the method*/
		Exception exception = assertThrows(HotelException.class, () ->  hotelService.getHotelById(hotel.getHotelId()));
		/*checking that the message is also the same as the expected message*/
		assertEquals("No hotel found with the id "+hotel.getHotelId(), exception.getMessage());
		verify(hotelDao, times(1)).findById(anyLong());
	}

	@Test
	@Order(4)
	public void testGetHotelsNearMe() {
	    Customer customer = new Customer(1, "prateek", "prateek@gmail.com", "9315807215", Gender.MALE, LocalDate.of(2002, 04, 26), "prateek@123", hotel.getAddress(), Role.CUSTOMER.name(), null, LocalDateTime.now());
	    SecurityContextHolder.setContext(securityContext);
	    when(securityContext.getAuthentication()).thenReturn(authentication);
		when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("prateek@gmail.com");
		when(customerDao.findByEmail(anyString())).thenReturn(Optional.of(
				customer
				));
		when(hotelDao.findByAddress(hotel.getAddress())).thenReturn(List.of(hotel));
		List<Hotel> hotels = hotelService.getHotelsNearMe();
		for(Hotel hotel:hotels) {
			assertEquals(customer.getAddress(), hotel.getAddress());
		}
		verify(customerDao, times(1)).findByEmail("prateek@gmail.com");
		verify(hotelDao, times(1)).findByAddress(customer.getAddress());
	}
	
	@Test
	@Order(5)
	public void testUpdateEmail() {
		
	    SecurityContextHolder.setContext(securityContext);
	    when(securityContext.getAuthentication()).thenReturn(authentication);
		when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("myhotel@gmail.com");
		when(hotelDao.findByHotelEmail(anyString())).thenReturn(Optional.of(hotel));
		when(hotelDao.setEmailOfHotel(anyLong(), anyString())).thenReturn(1);
		hotel.setHotelEmail("mynewhotel@gmail.com");
		when(hotelDao.findById(1L)).thenReturn(Optional.of(hotel));
		UpdateHotelDetailsDTO updateRequest = new UpdateHotelDetailsDTO("mynewhotel@gmail.com", "1234");
        when(passwordEncoder.matches(updateRequest.getPassword(), hotel.getPassword())).thenReturn(new BCryptPasswordEncoder().matches(updateRequest.getPassword(), hotel.getPassword()));
		
		Hotel result = hotelService.updateEmail(updateRequest);
		assertEquals("mynewhotel@gmail.com", result.getHotelEmail());
		
		verify(hotelDao, times(1)).findById(1L);
		verify(hotelDao, times(1)).setEmailOfHotel(1L, "mynewhotel@gmail.com");
		verify(hotelDao, times(1)).findByHotelEmail("myhotel@gmail.com");
		
	}
	
	
	@Test
	@Order(6)
	public void testUpdatePhone() {
		
	    SecurityContextHolder.setContext(securityContext);
	    when(securityContext.getAuthentication()).thenReturn(authentication);
		when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("myhotel@gmail.com");
		when(hotelDao.findByHotelEmail(anyString())).thenReturn(Optional.of(hotel));
		when(hotelDao.setPhoneOfHotel(anyLong(), anyString())).thenReturn(1);
		hotel.setHotelPhone("9315807215");
		when(hotelDao.findById(1L)).thenReturn(Optional.of(hotel));
		UpdateHotelDetailsDTO updateRequest = new UpdateHotelDetailsDTO("9315807215", "1234");
        when(passwordEncoder.matches(updateRequest.getPassword(), hotel.getPassword())).thenReturn(new BCryptPasswordEncoder().matches(updateRequest.getPassword(), hotel.getPassword()));
		
		Hotel result = hotelService.updatePhone(updateRequest);
		assertEquals("9315807215", result.getHotelPhone());
		
		verify(hotelDao, times(1)).findById(1L);
		verify(hotelDao, times(1)).setPhoneOfHotel(1L, "9315807215");
		verify(hotelDao, times(1)).findByHotelEmail("myhotel@gmail.com");
		
	}
	
	@Test
	@Order(7)
	public void testUpdateTelephone() {
		
	    SecurityContextHolder.setContext(securityContext);
	    when(securityContext.getAuthentication()).thenReturn(authentication);
		when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("myhotel@gmail.com");
		when(hotelDao.findByHotelEmail(anyString())).thenReturn(Optional.of(hotel));
		when(hotelDao.setTelephoneOfHotel(anyLong(), anyString())).thenReturn(1);
		hotel.setHotelTelephone("1234567890");
		when(hotelDao.findById(1L)).thenReturn(Optional.of(hotel));
		UpdateHotelDetailsDTO updateRequest = new UpdateHotelDetailsDTO("1234567890", "1234");
        when(passwordEncoder.matches(updateRequest.getPassword(), hotel.getPassword())).thenReturn(new BCryptPasswordEncoder().matches(updateRequest.getPassword(), hotel.getPassword()));
		
		Hotel result = hotelService.updateTelephone(updateRequest);
		assertEquals("1234567890", result.getHotelTelephone());
		
		verify(hotelDao, times(1)).findById(1L);
		verify(hotelDao, times(1)).setTelephoneOfHotel(1L, "1234567890");
		verify(hotelDao, times(1)).findByHotelEmail("myhotel@gmail.com");
		
	}
	
	@Test
	@Order(8)
	public void testUpdateName() {
		
	    SecurityContextHolder.setContext(securityContext);
	    when(securityContext.getAuthentication()).thenReturn(authentication);
		when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn("myhotel@gmail.com");
		when(hotelDao.findByHotelEmail(anyString())).thenReturn(Optional.of(hotel));
		when(hotelDao.setNameOfHotel(anyLong(), anyString())).thenReturn(1);
		hotel.setName("mynewhotel");
		when(hotelDao.findById(1L)).thenReturn(Optional.of(hotel));
		UpdateHotelDetailsDTO updateRequest = new UpdateHotelDetailsDTO("mynewhotel", "1234");
        when(passwordEncoder.matches(updateRequest.getPassword(), hotel.getPassword())).thenReturn(new BCryptPasswordEncoder().matches(updateRequest.getPassword(), hotel.getPassword()));
		
		Hotel result = hotelService.updateName(updateRequest);
		assertEquals("mynewhotel", result.getName());
		
		verify(hotelDao, times(1)).findById(1L);
		verify(hotelDao, times(1)).setNameOfHotel(1L, "mynewhotel");
		verify(hotelDao, times(1)).findByHotelEmail("myhotel@gmail.com");
		
	}

	@Test
	@Order(10)
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
	@Order(9)
	public void testGetHotelsInCity() {
		
		Address address = new Address("Near Hospital", "Jaipur", "RJ", "123456", "India");

		Customer customer = new Customer();
		customer.setAddress(address);
				
		when(hotelDao.findByAddress(any())).thenReturn(new ArrayList<>());
		
		assertThrows(HotelException.class, ()-> hotelService.getHotelsInCity("Jaipur"));
		
	}
	
}
