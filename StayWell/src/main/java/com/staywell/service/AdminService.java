package com.staywell.service;

import com.staywell.exception.AdminException;
import com.staywell.model.Admin;

public interface AdminService {
	public Admin registerAdmin(Admin admin) throws AdminException;
	
}
