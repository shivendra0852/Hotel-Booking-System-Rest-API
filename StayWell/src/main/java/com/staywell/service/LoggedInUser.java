package com.staywell.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import com.staywell.model.Admin;
import com.staywell.model.Customer;
import com.staywell.model.Hotel;
import com.staywell.repository.AdminDao;
import com.staywell.repository.CustomerDao;
import com.staywell.repository.HotelDao;

public class LoggedInUser {

	@Autowired
	private static HotelDao hotelDao;
	
	@Autowired
	private static AdminDao adminDao;
	
	@Autowired
	private static CustomerDao userDao;
	
	private static String email = SecurityContextHolder.getContext().getAuthentication().getName();
	
	public static Hotel getHotel() {
		return hotelDao.findByHotelEmail(email).get();
	}
	
	public static Admin getAdmin() {
		return adminDao.findByEmail(email).get();
	}
	
	public static Customer getCustomer() {
		return userDao.findByEmail(email).get();
	}
	
	
	
}
