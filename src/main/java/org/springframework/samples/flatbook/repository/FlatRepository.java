package org.springframework.samples.flatbook.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.flatbook.model.Flat;

import java.util.Set;

public interface FlatRepository {

    Set<Flat> findAll() throws DataAccessException;

    Flat findById(int id) throws DataAccessException;

//    Set<Flat> findByHostId(int hostId) throws DataAccessException;

    Set<Flat> findByCity(String city) throws DataAccessException;

    Set<Flat> findByCityAndPostalCode(String city, Integer postalCode) throws DataAccessException;

    void save(Flat flat) throws DataAccessException;
}
