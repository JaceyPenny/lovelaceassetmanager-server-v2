package io.lovelacetech.lovelaceassetmanager.dataaccess.repository;

import java.sql.Connection;
import java.util.Collection;
import java.util.UUID;
import java.util.function.Consumer;

import io.lovelacetech.lovelaceassetmanager.dataaccess.entities.BaseEntity;

public interface BaseRepositoryInterface<T extends BaseEntity<T>> {
	int count();
	T get(UUID id);
	Collection<T> all();
	boolean exists(UUID id);
	String getPrimaryTableName();
	void saveMany(Collection<T> allToSave);
	Collection<T> getMany(Collection<UUID> ids);
	void deleteMany(Collection<T> allToDelete);
	Collection<T> inRange(int limit, int offset);
	void connectAndRun(Consumer<Connection> action);
}
