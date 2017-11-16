package io.lovelacetech.lovelaceassetmanager.dataaccess.repository;

public enum DatabaseTable {
	NONE(""),
	COMPANY("company"),
	PRODUCT("product"),
	EMPLOYEE("employee");
	
	public String getLabel() {
		return label;
	}
	
	private final String label;
	
	private DatabaseTable(String label) {
		this.label = label;
	}
}
