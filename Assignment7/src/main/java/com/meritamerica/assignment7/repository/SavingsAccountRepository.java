package com.meritamerica.assignment7.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meritamerica.assignment7.models.SavingsAccount;
import com.meritamerica.assignment7.security.models.User;

public interface SavingsAccountRepository extends JpaRepository<SavingsAccount,Integer>{
	//Optional<SavingsAccount> findByIsOpen(Boolean isOpen);
}

