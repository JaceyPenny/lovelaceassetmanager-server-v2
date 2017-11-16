package io.lovelacetech.lovelaceassetmanager.models.repositories;

import io.lovelacetech.lovelaceassetmanager.dataaccess.repository.BaseRepository;
import io.lovelacetech.lovelaceassetmanager.dataaccess.repository.DatabaseTable;
import io.lovelacetech.lovelaceassetmanager.dataaccess.repository.helpers.PostgreFunctionType;
import io.lovelacetech.lovelaceassetmanager.dataaccess.repository.helpers.SQLComparisonType;
import io.lovelacetech.lovelaceassetmanager.dataaccess.repository.helpers.where.WhereClause;
import io.lovelacetech.lovelaceassetmanager.dataaccess.repository.helpers.where.WhereContainer;
import io.lovelacetech.lovelaceassetmanager.models.entities.CompanyEntity;
import io.lovelacetech.lovelaceassetmanager.models.entities.fieldnames.CompanyFieldNames;
import io.lovelacetech.lovelaceassetmanager.models.repositories.interfaces.CompanyRepositoryInterface;

import java.sql.SQLException;

public class CompanyRepository extends BaseRepository<CompanyEntity> implements CompanyRepositoryInterface {
    public CompanyRepository() {
        super(DatabaseTable.COMPANY);
    }

    @Override
    public CompanyEntity createOne() {
        return new CompanyEntity();
    }

    @Override
    public CompanyEntity byCredentials(String email, String password) {
        return this.firstOrDefaultWhere(
                new WhereContainer(
                        new WhereClause()
                                .postgreFunction(PostgreFunctionType.LOWER)
                                .table(this.primaryTable)
                                .fieldName(CompanyFieldNames.EMAIL)
                                .comparison(SQLComparisonType.EQUALS)
                ).addWhereClause(new WhereClause()
                        .postgreFunction(PostgreFunctionType.LOWER)
                        .table(this.primaryTable)
                        .fieldName(CompanyFieldNames.PASSWORD)
                        .comparison(SQLComparisonType.EQUALS)
                ), (ps) -> {
                    try {
                        ps.setObject(1, email.toLowerCase());
                        ps.setObject(2, password);
                    } catch (SQLException ignored) {
                    }
                    return ps;
                }
        );
    }
}
