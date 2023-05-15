package com.staywell.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.staywell.config.JwtTokenGeneratorFilter;
import com.staywell.enums.Role;
import com.staywell.exception.AdminException;
import com.staywell.exception.AuthenticationException;
import com.staywell.model.Admin;
import com.staywell.repository.AdminDao;

import lombok.Value;

public class AdminServiceImpl implements AdminService,LoginService{
	@Autowired
	private AdminDao aDao;
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtTokenGeneratorFilter jwtTokenGeneratorFilter;
	
	@Autowired
	private CustomerDetailsService customerDetailsService;
	

	@Override
	public Admin registerAdmin(Admin admin) throws AdminException {
		Optional<Admin> adminExist = aDao.findByEmail(admin.getEmail());
		
		if(adminExist.isPresent()) {
			throw new AdminException("Admin already registered with this email!");
		}
		
		String hashedPassword = passwordEncoder.encode(admin.getPassword());
	    admin.setPassword(hashedPassword);
	    
	    admin.setRole(Role.ADMIN);
	    
	    return aDao.save(admin);
	}

//	@Override
//	public String login(String email, String password) {
//		
//		try {
//            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
//            User user = userDao.findByEmail(email);
//            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));
//            jwtTokenGeneratorFilter.doFilterInternal(null, null, null);
//            return response.getHeader(SecurityConstants.JWT_HEADER);
//        } catch (AuthenticationException e) {
//            throw new BadCredentialsException("Invalid email/password supplied");
//        }
//	}

}
