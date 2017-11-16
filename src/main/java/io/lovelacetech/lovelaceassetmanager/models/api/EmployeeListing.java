package io.lovelacetech.lovelaceassetmanager.models.api;

import java.util.LinkedList;
import java.util.List;

public class EmployeeListing {
	private List<Employee> employees;
	public List<Employee> getEmployees() {
		return this.employees;
	}
	public EmployeeListing setEmployees(List<Employee> employees) {
		this.employees = employees;
		return this;
	}
	public EmployeeListing addEmployee(Employee employee) {
		this.employees.add(employee);
		return this;
	}
	
	public EmployeeListing() {
		this.employees = new LinkedList<Employee>();
	}
}