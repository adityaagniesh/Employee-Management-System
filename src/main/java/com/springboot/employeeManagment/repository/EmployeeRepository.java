package com.springboot.employeeManagment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.employeeManagment.entity.Employee;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

	// that's it ... no need to write any code LOL!

    // add a method to sort by last name
    public List<Employee> findAllByOrderByLastNameAsc();

}
