package io.lovelacetech.lovelaceassetmanager.models.entities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import io.lovelacetech.lovelaceassetmanager.dataaccess.entities.BaseEntity;
import io.lovelacetech.lovelaceassetmanager.models.api.Employee;
import io.lovelacetech.lovelaceassetmanager.models.entities.fieldnames.EmployeeFieldNames;
import io.lovelacetech.lovelaceassetmanager.models.repositories.EmployeeRepository;

public class EmployeeEntity extends BaseEntity<EmployeeEntity> {
	private String firstName;
	private String lastName;
	private String employeeId;
	private boolean active;
	private String role;
	private String manager;
	private String password;
	private LocalDateTime createdOn;
	
	@Override
	protected void fillFromRecord(ResultSet rs) throws SQLException {
		this.firstName = rs.getString(EmployeeFieldNames.FIRST_NAME);
		this.lastName = rs.getString(EmployeeFieldNames.LAST_NAME);
		this.employeeId = rs.getString(EmployeeFieldNames.EMPLOYEE_ID);
		this.active = rs.getBoolean(EmployeeFieldNames.ACTIVE);
		this.role = rs.getString(EmployeeFieldNames.ROLE);
		this.manager = rs.getString(EmployeeFieldNames.MANAGER);
		this.password = rs.getString(EmployeeFieldNames.PASSWORD);
		this.createdOn = rs.getTimestamp(EmployeeFieldNames.CREATED_ON).toLocalDateTime();
	}

	@Override
	protected Map<String, Object> fillRecord(Map<String, Object> record) {
		record.put(EmployeeFieldNames.FIRST_NAME, this.firstName);
		record.put(EmployeeFieldNames.LAST_NAME, this.lastName);
		record.put(EmployeeFieldNames.EMPLOYEE_ID, this.employeeId);
		record.put(EmployeeFieldNames.ACTIVE, this.active);
		record.put(EmployeeFieldNames.ROLE, this.role);
		record.put(EmployeeFieldNames.MANAGER, this.manager);
		record.put(EmployeeFieldNames.PASSWORD, this.password);
		record.put(EmployeeFieldNames.CREATED_ON, Timestamp.valueOf(this.createdOn));
		return record;
	}
	
	public String getFirstName()
	{
		return this.firstName;
	}
	public EmployeeEntity setFirstName(String firstName)
	{
		if (!StringUtils.equals(this.firstName, firstName)) {
			this.firstName = firstName;
			this.propertyChanged(EmployeeFieldNames.FIRST_NAME);
		}
		return this; 
	}
	
	public String getLastName()
	{
		return this.lastName;
	}
	public EmployeeEntity setLastName(String lastName)
	{
		if (!StringUtils.equals(this.lastName, lastName)) {
			this.lastName = lastName;
			this.propertyChanged(EmployeeFieldNames.LAST_NAME);
		}
		return this;
	}
	
	public String getEmployeeID() {
		return this.employeeId;
	}
	public EmployeeEntity setEmployeeID(String employeeId) {
		if (!StringUtils.equals(this.employeeId, employeeId)) {
			this.employeeId = employeeId;
			this.propertyChanged(EmployeeFieldNames.EMPLOYEE_ID);
		}
		return this;
	}
	
	public boolean getActive()
	{
		return this.active;
	}
	public EmployeeEntity setActive(boolean active)
	{
		if (this.active == active)
		{
			this.active = active;
			this.propertyChanged(EmployeeFieldNames.ACTIVE);
		}
		return this;
	}
	
	public String getRole()
	{
		return this.role;
	}
	public EmployeeEntity setRole(String role)
	{
		if (!StringUtils.equals(this.role, role)) {
			this.role = role;
			this.propertyChanged(EmployeeFieldNames.ROLE);
		}
		return this;
	}
	
	public String getManager()
	{
		return this.manager;
	}
	public EmployeeEntity setManager(String manager)
	{
		if (!StringUtils.equals(this.manager, manager)) {
			this.manager = manager;
			this.propertyChanged(EmployeeFieldNames.MANAGER);
		}
		return this;
	}

	public String getPassword()
	{
		return this.password;
	}
	public EmployeeEntity setPassword(String password)
	{
		if (!StringUtils.equals(this.password, password)) {
			this.password = password;
			this.propertyChanged(EmployeeFieldNames.PASSWORD);
		}
		return this;
	}
	
	public LocalDateTime getCreatedOn() {
		return this.createdOn;
	}
	
	public Employee synchronize(Employee apiEmployee) {
		this.setFirstName(apiEmployee.getFirstName());
		this.setLastName(apiEmployee.getLastName());
		this.setEmployeeID(apiEmployee.getEmployeeId());
		this.setActive(apiEmployee.getActive());
		this.setRole(apiEmployee.getRole());
		this.setManager(apiEmployee.getManager());
		this.setPassword(apiEmployee.getPassword());
		apiEmployee.setCreatedOn(this.createdOn);
		
		return apiEmployee;
	}
	
	public EmployeeEntity() {
		super(new EmployeeRepository());
		
		this.firstName = StringUtils.EMPTY;
		this.lastName = StringUtils.EMPTY;
		this.employeeId = StringUtils.EMPTY;
		this.active = false;
		this.role = StringUtils.EMPTY;
		this.manager = StringUtils.EMPTY;
		this.password = StringUtils.EMPTY;
		this.createdOn = LocalDateTime.now();
	}
	
	public EmployeeEntity(UUID id) {
		super(id, new EmployeeRepository());
		
		this.firstName = StringUtils.EMPTY;
		this.lastName = StringUtils.EMPTY;
		this.employeeId = StringUtils.EMPTY;
		this.active = false;
		this.role = StringUtils.EMPTY;
		this.manager = StringUtils.EMPTY;
		this.password = StringUtils.EMPTY;
		this.createdOn = LocalDateTime.now();
	}

	public EmployeeEntity(Employee apiEmployee) {
		super(apiEmployee.getId(), new EmployeeRepository());
		
		this.firstName = apiEmployee.getFirstName();
		this.lastName = apiEmployee.getLastName();
		this.employeeId = apiEmployee.getEmployeeId();
		this.active = apiEmployee.getActive();
		this.role = apiEmployee.getRole();
		this.manager = apiEmployee.getManager();
		this.password = apiEmployee.getPassword();
		this.createdOn = LocalDateTime.now();
	}
}

