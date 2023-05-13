package com.staywell.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.staywell.model.Room;

public interface RoomDao extends JpaRepository<Room, Integer>{

}
