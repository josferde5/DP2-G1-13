
package org.springframework.samples.flatbook.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.flatbook.model.Host;

public interface HostRepository {

	Collection<Host> findAll() throws DataAccessException;

	Host findByUsername(String username) throws DataAccessException;

	void save(Host host) throws DataAccessException;
}
