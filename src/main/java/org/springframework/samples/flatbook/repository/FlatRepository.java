
package org.springframework.samples.flatbook.repository;

import java.util.Set;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.samples.flatbook.model.Flat;

public interface FlatRepository {

	Set<Flat> findAll() throws DataAccessException;

	Flat findById(int id) throws DataAccessException;

	Set<Flat> findByHostUsername(String username) throws DataAccessException;

	void save(Flat flat) throws DataAccessException;

    void delete(Flat flat);
    void deleteById(int id);

	Flat findByReviewId(Integer reviewId) throws DataAccessException;

	Page<Flat> topBestReviewedFlats(Pageable pageable) throws DataAccessException;

	Page<Flat> topWorstReviewedFlats(Pageable pageable) throws DataAccessException;

	Integer numberOfFlats() throws DataAccessException;

	Double ratioOfFlatsWithAdvertisement() throws DataAccessException;

	Flat findFlatWithRequestId(int requestId) throws DataAccessException;

}
