package com.staywell.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.staywell.model.Admin;

public interface AdminDao extends JpaRepository<Admin,Integer>{
	Optional<Admin> findByEmail(String email);
}
