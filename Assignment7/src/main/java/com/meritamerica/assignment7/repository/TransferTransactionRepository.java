package com.meritamerica.assignment7.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meritamerica.assignment7.models.TransferTransaction;

public interface TransferTransactionRepository extends JpaRepository <TransferTransaction,Integer> {

}
