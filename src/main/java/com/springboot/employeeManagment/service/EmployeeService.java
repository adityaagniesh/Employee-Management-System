package com.springboot.employeeManagment.service;

import java.util.List;

import com.springboot.employeeManagment.entity.Employee;

public interface EmployeeService {

	List<Employee> findAll();
	
	Employee findById(int theId);
	
	void save(Employee theEmployee);
	
	void deleteById(int theId);
	
}
