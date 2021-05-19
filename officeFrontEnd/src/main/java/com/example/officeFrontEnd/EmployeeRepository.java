package com.example.officeFrontEnd;
import org.springframework.data.jpa.repository.JpaRepository;

interface EmployeeRepository extends JpaRepository<Employee, Long>{

    Employee findByUsername(String username);

}
