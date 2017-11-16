package io.lovelacetech.lovelaceassetmanager.commands.employees;

import java.util.Random;
import java.util.UUID;

import io.lovelacetech.lovelaceassetmanager.commands.ResultCommandInterface;
import io.lovelacetech.lovelaceassetmanager.models.entities.EmployeeEntity;
import io.lovelacetech.lovelaceassetmanager.models.repositories.EmployeeRepository;
import org.apache.commons.lang3.StringUtils;

import io.lovelacetech.lovelaceassetmanager.models.api.Employee;
import io.lovelacetech.lovelaceassetmanager.models.api.enums.EmployeeApiRequestStatus;
import io.lovelacetech.lovelaceassetmanager.models.repositories.interfaces.EmployeeRepositoryInterface;

public class EmployeeSaveCommand implements ResultCommandInterface<Employee> {
	@Override
	public Employee execute() {
		if (StringUtils.isBlank(this.apiEmployee.getEmployeeId())) {
			return (new Employee()).setApiRequestStatus(EmployeeApiRequestStatus.INVALID_INPUT);
		}

		EmployeeEntity employeeEntity = this.employeeRepository.get(this.apiEmployee.getId());
		if (employeeEntity != null) {
			this.apiEmployee = employeeEntity.synchronize(this.apiEmployee);
		} else {
			employeeEntity = this.employeeRepository.byCredentials(this.apiEmployee.getEmployeeId(), this.apiEmployee.getPassword());
			if (employeeEntity == null) {
				this.apiEmployee.setEmployeeID(generateID());
				employeeEntity = new EmployeeEntity(this.apiEmployee);
			} else {
				return (new Employee()).setApiRequestStatus(EmployeeApiRequestStatus.EMPLOYEE_ALREADY_EXISTS);
			}
		}

		employeeEntity.save();
		if ((new UUID(0, 0)).equals(this.apiEmployee.getId())) {
			this.apiEmployee.setId(employeeEntity.getId());
		}

		return this.apiEmployee;
	}

	public String generateID()
	{
		char[] chars = "0123456789".toCharArray();
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		
		for (int i = 0; i < 10; i++) {
		    char c = chars[random.nextInt(chars.length)];
		    sb.append(c);
		}
		
		return sb.toString();
	}
	
	// Properties
	private Employee apiEmployee;

	public Employee getApiEmployee() {
		return this.apiEmployee;
	}

	public EmployeeSaveCommand setApiEmployee(Employee apiEmployee) {
		this.apiEmployee = apiEmployee;
		return this;
	}

	private EmployeeRepositoryInterface employeeRepository;

	public EmployeeRepositoryInterface getEmployeeRepository() {
		return this.employeeRepository;
	}

	public EmployeeSaveCommand setEmployeeRepository(EmployeeRepositoryInterface employeeRepository) {
		this.employeeRepository = employeeRepository;
		return this;
	}

	public EmployeeSaveCommand() {
		this.employeeRepository = new EmployeeRepository();
	}
}
