package com.hapinistay.backend.service;


import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.hapinistay.backend.model.House;
import com.hapinistay.backend.model.User;

public interface UserService {
	
	User findById(Long id);

	User findByUsername(String username);

	void saveUser(User user);

	void updateUser(User user);

	void deleteUserById(Long id);

	void deleteAllUsers();

	List<User> findAllUsers();

	boolean isUserExist(User user);
	
	public User loadUserByUsername(String username) throws UsernameNotFoundException;
	
	public User removeHouseOfUser(Long user_id, Long house_id);
	
	public User removeApartmentOfUser(Long userId, Long apartmentId);
}