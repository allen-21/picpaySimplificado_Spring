package com.picpaySimplificado.repositories;

import com.picpaySimplificado.domain.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository <Transaction, Long> {
}
