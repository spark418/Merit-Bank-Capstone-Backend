package com.meritamerica.assignment7.security.services;

import java.util.List;

import com.meritamerica.assignment7.models.CheckingAccount;
import com.meritamerica.assignment7.models.SavingsAccount;
import com.meritamerica.assignment7.security.models.User;

public interface UserService {
	public User addUser(User user);
	public User getUser(int userId);
	public User getUserByUserName(String username);
	public User updateUser(User user,int id);
}
