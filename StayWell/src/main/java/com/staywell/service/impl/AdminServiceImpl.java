package com.staywell.service.impl;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.staywell.enums.Role;
import com.staywell.exception.AdminException;
import com.staywell.model.Admin;
import com.staywell.repository.AdminDao;
import com.staywell.service.AdminService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
@Transactional
public class AdminServiceImpl implements AdminService {

	private AdminDao aDao;
	private PasswordEncoder passwordEncoder;

	@Override
	public Admin registerAdmin(Admin admin) throws AdminException {
		Optional<Admin> adminExist = aDao.findByEmail(admin.getEmail());

		if (adminExist.isPresent()) {
			throw new AdminException("Admin already registered with this email!");
		}

		String hashedPassword = passwordEncoder.encode(admin.getPassword());
		admin.setPassword(hashedPassword);
		admin.setRole(Role.ROLE_ADMIN);

		return aDao.save(admin);
	}

}
