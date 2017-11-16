package io.lovelacetech.lovelaceassetmanager.controllers;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.lovelacetech.lovelaceassetmanager.commands.employees.EmployeeByCredentialsQuery;
import io.lovelacetech.lovelaceassetmanager.commands.employees.EmployeeQuery;
import io.lovelacetech.lovelaceassetmanager.commands.employees.EmployeeSaveCommand;
import io.lovelacetech.lovelaceassetmanager.commands.employees.EmployeesQuery;
import io.lovelacetech.lovelaceassetmanager.models.api.Employee;
import io.lovelacetech.lovelaceassetmanager.models.api.EmployeeListing;

@RestController
@RequestMapping(value = "/employee")
public class EmployeeRestController {
	@RequestMapping(value = "/apiv0/{employeeId}/{password}", method = RequestMethod.GET)
	public Employee getEmployee(@PathVariable String employeeId, @PathVariable String password) {
		return (new EmployeeQuery())
				.setEmployeeId(employeeId)
				.setPassword(password)
				.execute();
	}

	@RequestMapping(value = "/apiv0/byCredentials/{employeeId}/{password}", method = RequestMethod.GET)
	public Employee getEmployeeByCredentials(@PathVariable String employeeId, @PathVariable String password) {
		return new EmployeeByCredentialsQuery()
				.setEmployeeId(employeeId)
				.setPassword(password)
				.execute();
	}

	@RequestMapping(value = "/apiv0/employees", method = RequestMethod.GET)
	public EmployeeListing getEmployees() { 
		return new EmployeesQuery().execute();
	}
	
	@RequestMapping(value = "/apiv0/", method = RequestMethod.PUT)
	public Employee putActivity(@RequestBody Employee employee) {
		return new EmployeeSaveCommand()
				.setApiEmployee(employee)
				.execute();
	}

	@ResponseBody
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public String test() {
		return "Successful test. (EmployeeRestController)";
	}
}