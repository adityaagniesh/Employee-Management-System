package com.springboot.employeeManagment.controller;

import java.util.List;

import com.springboot.employeeManagment.service.EmployeeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.springboot.employeeManagment.entity.Employee;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;


	@GetMapping("/user/list")
	@PreAuthorize("hasRole('USER')")
	public String listEmployees(Model theModel) {

		// get the employees from db
		List<Employee> theEmployees = employeeService.findAll();

		// add to the spring model
		theModel.addAttribute("employees", theEmployees);

		return "employees/user-employee";
	}

	@GetMapping("/admin/list")
	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	public String listEmployeestoAdmin(Model theModel) {

		// get the employees from db
		List<Employee> theEmployees = employeeService.findAll();

		// add to the spring model
		theModel.addAttribute("employees", theEmployees);

		return "employees/list-employees";
	}

	@GetMapping("/admin/showFormForAdd")
	@PreAuthorize("hasRole('ADMIN')")
	public String showFormForAdd(Model theModel) {

		// create model attribute to bind form data
		Employee theEmployee = new Employee();

		theModel.addAttribute("employee", theEmployee);

		return "employees/employee-form";
	}

	@PostMapping("/admin/showFormForUpdate")
	@PreAuthorize("hasRole('ADMIN')")
	public String showFormForUpdate(@RequestParam("employeeId") int theId,
									Model theModel) {

		// get the employee from the service
		Employee theEmployee = employeeService.findById(theId);

		// set employee as a model attribute to pre-populate the form
		theModel.addAttribute("employee", theEmployee);

		// send over to our form
		return "employees/employee-form";
	}

	@PostMapping("/admin/save")
	@PreAuthorize("hasRole('ADMIN')")
	public String saveEmployee(@ModelAttribute("employee") Employee theEmployee) {

		// save the employee
		employeeService.save(theEmployee);

		// use a redirect to prevent duplicate submissions
		return "redirect:/employees/admin/list";
	}

	@PostMapping("/admin/delete")
	@PreAuthorize("hasRole('ADMIN')")
	public String delete(@RequestParam("employeeId") int theId) {

		// delete the employee
		employeeService.deleteById(theId);

		// redirect to /employees/list
		return "redirect:/employees/admin/list";

	}
}









