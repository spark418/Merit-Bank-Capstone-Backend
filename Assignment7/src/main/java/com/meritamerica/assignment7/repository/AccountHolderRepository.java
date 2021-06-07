package com.meritamerica.assignment7.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meritamerica.assignment7.models.AccountHolder;

public interface AccountHolderRepository extends JpaRepository<AccountHolder,Integer> {

}
