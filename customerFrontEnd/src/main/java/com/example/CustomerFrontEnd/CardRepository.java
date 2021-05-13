package com.example.CustomerFrontEnd;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

interface CardRepository extends JpaRepository<Card, String>{
    List<Card> findByCustomerId(Long customerId);
    Optional<Card> findByCardNumberAndCustomerId(String id, Long customerId);
}
