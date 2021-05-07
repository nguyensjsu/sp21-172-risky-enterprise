
package com.example.OrderProcessing;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByRegId(Long regId);
}