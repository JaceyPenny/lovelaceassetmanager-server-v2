package io.lovelacetech.lovelaceassetmanager.models.repositories;

import java.sql.SQLException;

import io.lovelacetech.lovelaceassetmanager.dataaccess.repository.BaseRepository;
import io.lovelacetech.lovelaceassetmanager.dataaccess.repository.DatabaseTable;
import io.lovelacetech.lovelaceassetmanager.dataaccess.repository.helpers.PostgreFunctionType;
import io.lovelacetech.lovelaceassetmanager.dataaccess.repository.helpers.SQLComparisonType;
import io.lovelacetech.lovelaceassetmanager.dataaccess.repository.helpers.where.WhereClause;
import io.lovelacetech.lovelaceassetmanager.dataaccess.repository.helpers.where.WhereContainer;
import io.lovelacetech.lovelaceassetmanager.models.entities.EmployeeEntity;
import io.lovelacetech.lovelaceassetmanager.models.entities.fieldnames.EmployeeFieldNames;
import io.lovelacetech.lovelaceassetmanager.models.repositories.interfaces.EmployeeRepositoryInterface;

public class EmployeeRepository extends BaseRepository<EmployeeEntity> implements EmployeeRepositoryInterface {
	@Override
	public EmployeeEntity byCredentials(String employeeID, String password) {
		return this.firstOrDefaultWhere(
			new WhereContainer(
				new WhereClause()
					.postgreFunction(PostgreFunctionType.LOWER)
					.table(this.primaryTable)
					.fieldName(EmployeeFieldNames.EMPLOYEE_ID)
					.comparison(SQLComparisonType.EQUALS)
			).addWhereClause(
				new WhereClause()
					.postgreFunction(PostgreFunctionType.LOWER)
					.table(this.primaryTable)
					.fieldName(EmployeeFieldNames.PASSWORD)
					.comparison(SQLComparisonType.EQUALS)
			),
			(ps) -> {
				try {
					ps.setObject(1, employeeID.toLowerCase());
					ps.setObject(2, password);
				} catch (SQLException e) {}

				return ps;
			}
		);
	}
	
	@Override
	public EmployeeEntity createOne() {
		return new EmployeeEntity();
	}
	
	public EmployeeRepository() {
		super(DatabaseTable.EMPLOYEE);
	}
}
