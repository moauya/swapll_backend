package com.swapll.gradu.repository;

import com.swapll.gradu.model.Enum.TransactionStatus;
import com.swapll.gradu.model.Transaction;
import com.swapll.gradu.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    List<Transaction> findBySellerAndStatus(User seller, TransactionStatus transactionStatus);
}
