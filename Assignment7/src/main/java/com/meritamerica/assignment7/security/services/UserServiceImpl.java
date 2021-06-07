package com.meritamerica.assignment7.security.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meritamerica.assignment7.security.models.User;
import com.meritamerica.assignment7.security.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired UserRepository userRepository;

	@Override
	public User addUser(User user) {
		return userRepository.save(user);
	}

	@Override
	public User getUser(int userId) {
		return userRepository.findById(userId).orElse(null);
	}

	@Override
	public User getUserByUserName(String username) {
		return userRepository.findByUserName(username).orElse(null);
	}
	
}
