package com.hapinistay.backend.service;

import java.util.List;

import com.hapinistay.backend.model.User;
import com.hapinistay.backend.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service("userService")
///@Transactional
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepository;

	public User findById(Long id) {
		return userRepository.findOne(id);
	}

	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	public void saveUser(User user) {
		userRepository.save(user);
	}

	public void updateUser(User user){
		saveUser(user);
	}

	public void deleteUserById(Long id){
		userRepository.delete(id);
	}

	public void deleteAllUsers(){
		userRepository.deleteAll();
	}

	public List<User> findAllUsers(){
		return userRepository.findAll();
	}

	public boolean isUserExist(User user) {
		return findByUsername(user.getUsername()) != null;
	}

	@Override
	public User loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);
		return user;
	}

	@Override
	public User removeHouseOfUser(Long user_id, Long house_id) {
		User user = this.findById(user_id);
		if(user != null) {
			user.getHouses().removeIf(house -> house.getId() == house_id);
			this.updateUser(user);
			return user;
		}
		return null;
	}

	@Override
	public User removeApartmentOfUser(Long userId, Long apartmentId) {
		User user = this.findById(userId);
		if(user != null) {
			user.getApartments().removeIf(apartment -> apartment.getId() == apartmentId);
			this.updateUser(user);
			return user;
		}
		return null;
	}

}
