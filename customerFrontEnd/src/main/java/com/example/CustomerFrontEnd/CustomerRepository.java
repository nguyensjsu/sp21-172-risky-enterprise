package com.example.CustomerFrontEnd;

import org.springframework.data.jpa.repository.JpaRepository;

interface CustomerRepository extends JpaRepository<Customer, Long>{

    Customer findByUsername(String username);

}
