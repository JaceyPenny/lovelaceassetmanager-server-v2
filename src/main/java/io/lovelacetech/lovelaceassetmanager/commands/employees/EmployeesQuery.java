package io.lovelacetech.lovelaceassetmanager.commands.employees;

import java.util.stream.Collectors;

import io.lovelacetech.lovelaceassetmanager.commands.ResultCommandInterface;
import io.lovelacetech.lovelaceassetmanager.models.api.Employee;
import io.lovelacetech.lovelaceassetmanager.models.api.EmployeeListing;
import io.lovelacetech.lovelaceassetmanager.models.repositories.EmployeeRepository;
import io.lovelacetech.lovelaceassetmanager.models.repositories.interfaces.EmployeeRepositoryInterface;

public class EmployeesQuery implements ResultCommandInterface<EmployeeListing> {
	@Override
	public EmployeeListing execute() {
		return (new EmployeeListing()).
			setEmployees(
				this.employeeRepository.
					all().
					stream().
					map(mp -> (new Employee(mp))).
					collect(Collectors.toList()
			)
		);
	}

	//Properties
	private EmployeeRepositoryInterface employeeRepository;
	public EmployeeRepositoryInterface getEmployeeRepository() {
		return this.employeeRepository;
	}
	public EmployeesQuery setProductRepository(EmployeeRepositoryInterface employeeRepository) {
		this.employeeRepository = employeeRepository;
		return this;
	}
	
	public EmployeesQuery() {
		this.employeeRepository = new EmployeeRepository();
	}
}
