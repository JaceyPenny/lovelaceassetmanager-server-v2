package io.lovelacetech.lovelaceassetmanager.commands.employees;

import io.lovelacetech.lovelaceassetmanager.commands.ResultCommandInterface;
import io.lovelacetech.lovelaceassetmanager.models.api.Employee;
import io.lovelacetech.lovelaceassetmanager.models.repositories.EmployeeRepository;
import io.lovelacetech.lovelaceassetmanager.models.repositories.interfaces.EmployeeRepositoryInterface;

public class EmployeeQuery implements ResultCommandInterface<Employee> {
	@Override
	public Employee execute() {
		return new Employee(
			this.employeeRepository.byCredentials(this.employeeId, this.password)
		);
	}

	//Properties
	private String employeeId;
	private String password;
	public String getEmployeeId() {
		return this.employeeId;
	}
	public EmployeeQuery setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
		return this;
	}
	
	public String getPassword() {
		return this.password;
	}
	public EmployeeQuery setPassword(String password) {
		this.password = password;
		return this;
	}
	
	private EmployeeRepositoryInterface employeeRepository;
	public EmployeeRepositoryInterface getEmployeeRepository() {
		return this.employeeRepository;
	}
	public EmployeeQuery setEmployeeRepository(EmployeeRepositoryInterface employeeRepository) {
		this.employeeRepository = employeeRepository;
		return this;
	}
	
	public EmployeeQuery() {
		this.employeeRepository = new EmployeeRepository();
	}
}
