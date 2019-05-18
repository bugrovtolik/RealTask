package com.abugrov.realtask.repos;

import com.abugrov.realtask.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepo extends JpaRepository<Payment, Long> {
}
