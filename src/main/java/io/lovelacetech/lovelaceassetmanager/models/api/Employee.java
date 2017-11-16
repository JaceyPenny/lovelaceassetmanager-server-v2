package io.lovelacetech.lovelaceassetmanager.models.api;

import java.time.LocalDateTime;
import java.util.UUID;

import io.lovelacetech.lovelaceassetmanager.models.api.enums.EmployeeApiRequestStatus;
import io.lovelacetech.lovelaceassetmanager.models.entities.EmployeeEntity;
import org.apache.commons.lang3.StringUtils;

public class Employee {
	private UUID recordid;
	private String firstName;
	private String lastName;
	private String employeeId;
	private boolean active;
	private String role;
	private String manager;
	private String password;
	private LocalDateTime createdOn;
	private EmployeeApiRequestStatus apiRequestStatus;
	private String apiRequestMessage;
	
	public UUID getId() {
		return this.recordid;
	}
	public Employee setId(UUID id) {
		this.recordid = id;
		return this;
	}
	
	public String getFirstName()
	{
		return firstName;
	}
	public Employee setFirstName(String firstName)
	{
		this.firstName = firstName;
		return this;
	}
	
	public String getLastName()
	{
		return lastName;
	}
	public Employee setLastName(String lastName)
	{
		this.lastName = lastName;
		return this;
	}
	
	public String getEmployeeId() {
		return this.employeeId;
	}
	public Employee setEmployeeID(String employeeId) {
		this.employeeId = employeeId;
		return this;
	}
	
	public boolean getActive()
	{
		return active;
	}
	public Employee setActive(boolean active)
	{
		this.active = active;
		return this;
	}
	
	public String getRole()
	{
		return role;
	}
	public Employee setRole(String role)
	{
		this.role = role;
		return this;
	}
	
	public String getManager()
	{
		return manager;
	}
	public Employee setManager(String manager)
	{
		this.manager = manager;
		return this;
	}
	
	public String getPassword()
	{
		return password;
	}
	public Employee setPassword(String password)
	{
		this.password = password;
		return this;
	}
	
	public LocalDateTime getCreatedOn() {
		return this.createdOn;
	}
	public Employee setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
		return this;
	}
	
	
	public EmployeeApiRequestStatus getApiRequestStatus() {
		return this.apiRequestStatus;
	}
	public Employee setApiRequestStatus(EmployeeApiRequestStatus apiRequestStatus) {
		if (this.apiRequestStatus != apiRequestStatus) {
			this.apiRequestStatus = apiRequestStatus;
		}
		
		return this;
	}
	
	public String getApiRequestMessage() {
		return this.apiRequestMessage;
	}
	public Employee setApiRequestMessage(String apiRequestMessage) {
		if (!StringUtils.equalsIgnoreCase(this.apiRequestMessage, apiRequestMessage)) {
			this.apiRequestMessage = apiRequestMessage;
		}
		
		return this;
	}
	
	public Employee() {
		this.employeeId = "";
		this.firstName = "";
		this.lastName = "";
		this.active = false;
		this.recordid = new UUID(0, 0);
		this.role = "";
		this.manager = "";
		this.password = "";
		this.createdOn = LocalDateTime.now();
		this.apiRequestMessage = StringUtils.EMPTY;
		this.apiRequestStatus = EmployeeApiRequestStatus.OK;
		
	}
	
	public Employee(EmployeeEntity employeeEntity) {
		this.employeeId = employeeEntity.getEmployeeID();
		this.firstName = employeeEntity.getFirstName();
		this.lastName = employeeEntity.getLastName();
		this.active = employeeEntity.getActive();
		this.recordid = employeeEntity.getId();
		this.role = employeeEntity.getRole();
		this.manager = employeeEntity.getManager();
		this.password = employeeEntity.getPassword();
		this.createdOn = employeeEntity.getCreatedOn();
		this.apiRequestMessage = StringUtils.EMPTY;
		this.apiRequestStatus = EmployeeApiRequestStatus.OK;
	}
}

