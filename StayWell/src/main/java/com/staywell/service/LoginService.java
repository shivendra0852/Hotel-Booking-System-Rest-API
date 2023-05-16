package com.staywell.service;

import com.staywell.exception.AuthenticationException;

public interface LoginService {
	public String login(String email, String password) throws AuthenticationException;
}
