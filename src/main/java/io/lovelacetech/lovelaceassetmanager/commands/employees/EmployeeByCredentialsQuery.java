package io.lovelacetech.lovelaceassetmanager.commands.employees;

import io.lovelacetech.lovelaceassetmanager.commands.ResultCommandInterface;
import io.lovelacetech.lovelaceassetmanager.models.entities.EmployeeEntity;
import org.apache.commons.lang3.StringUtils;

import io.lovelacetech.lovelaceassetmanager.models.api.Employee;
import io.lovelacetech.lovelaceassetmanager.models.api.enums.EmployeeApiRequestStatus;
import io.lovelacetech.lovelaceassetmanager.models.repositories.EmployeeRepository;
import io.lovelacetech.lovelaceassetmanager.models.repositories.interfaces.EmployeeRepositoryInterface;

public class EmployeeByCredentialsQuery implements ResultCommandInterface<Employee> {
	@Override
	public Employee execute() {
		if (StringUtils.isBlank(this.employeeId)) {
			return new Employee().setApiRequestStatus(EmployeeApiRequestStatus.INVALID_INPUT);
		}
		
		EmployeeEntity employeeEntity = this.employeeRepository.byCredentials(this.employeeId, this.password);
		if (employeeEntity != null) {
			return new Employee(employeeEntity);
		} else {
			return new Employee().setApiRequestStatus(EmployeeApiRequestStatus.NOT_FOUND);
		}
	}

	//Properties
	private String employeeId;
	private String password;
	public String getEmployeeId() {
		return this.employeeId;
	}
	public EmployeeByCredentialsQuery setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
		return this;
	}
	
	public String getPassword() {
		return this.password;
	}
	public EmployeeByCredentialsQuery setPassword(String password) {
		this.password = password;
		return this;
	}
	
	private EmployeeRepositoryInterface employeeRepository;
	public EmployeeRepositoryInterface getEmployeeRepository() {
		return this.employeeRepository;
	}
	public EmployeeByCredentialsQuery setEmployeeRepository(EmployeeRepositoryInterface employeeRepository) {
		this.employeeRepository = employeeRepository;
		return this;
	}
	
	public EmployeeByCredentialsQuery() {
		this.employeeRepository = new EmployeeRepository();
	}
}
