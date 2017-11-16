package io.lovelacetech.lovelaceassetmanager.dataaccess.repository.helpers.join;

public enum SQLJoinType {
	NONE(""),
	INNER("INNER"),
	LEFT("LEFT");
	
	public String getLabel() {
		return label;
	}
	
	private final String label;
	
	private SQLJoinType(String label) {
		this.label = label;
	}
}
