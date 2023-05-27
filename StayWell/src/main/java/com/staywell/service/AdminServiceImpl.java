package com.staywell.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.staywell.exception.AdminException;
import com.staywell.model.Admin;
import com.staywell.repository.AdminDao;

@Service
public class AdminServiceImpl implements AdminService{

	@Autowired
	private AdminDao aDao;
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	

	@Override
	public Admin registerAdmin(Admin admin) throws AdminException {
		Optional<Admin> adminExist = aDao.findByEmail(admin.getEmail());
		
		if(adminExist.isPresent()) {
			throw new AdminException("Admin already registered with this email!");
		}
		
		String hashedPassword = passwordEncoder.encode(admin.getPassword());
	    admin.setPassword(hashedPassword);
	    
	    return aDao.save(admin);
	}

}
