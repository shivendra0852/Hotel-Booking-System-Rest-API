package com.staywell.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.staywell.model.Admin;
import com.staywell.service.AdminService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/staywell/admins")
@AllArgsConstructor
public class AdminController {

	private AdminService adminService;
	
	@PostMapping("/register")
	public ResponseEntity<Admin> registerAdmin(@RequestBody Admin admin){
		
		Admin res = adminService.registerAdmin(admin);
		
		return new ResponseEntity<Admin>(res,HttpStatus.CREATED);
	}
}
