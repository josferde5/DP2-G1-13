package org.springframework.samples.flatbook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.flatbook.model.Advertisement;
import org.springframework.samples.flatbook.repository.AdvertisementRepository;
import org.springframework.samples.flatbook.repository.DBImageRepository;
import org.springframework.samples.flatbook.repository.FlatRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Set;

@Service
public class AdvertisementService {

    private FlatRepository flatRepository;

    private AdvertisementRepository advertisementRepository;

    private DBImageRepository dbImageRepository;

    @Autowired
    public AdvertisementService(FlatRepository flatRepository, AdvertisementRepository advertisementRepository, DBImageRepository dbImageRepository) {
        this.flatRepository = flatRepository;
        this.advertisementRepository = advertisementRepository;
        this.dbImageRepository = dbImageRepository;
    }

    @Transactional(readOnly = true)
    public Advertisement findAdvertisementById(int id) throws DataAccessException {
        return this.advertisementRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Set<Advertisement> findAdvertisementsByCity(String city) throws DataAccessException {
        return this.advertisementRepository.findByCity(city);
    }

    @Transactional(readOnly = true)
    public Set<Advertisement> findAdvertisementsByCityAndPostalCode(String city, Integer postalCode) throws DataAccessException {
        return this.advertisementRepository.findByCityAndPostalCode(city, postalCode);
    }

    @Transactional(readOnly = true)
    public Set<Advertisement> findAdvertisementsByCityAndCountry(String city, String country) throws DataAccessException {
        return this.advertisementRepository.findByCityAndCountry(city, country);
    }

    @Transactional(readOnly = true)
    public Set<Advertisement> findAdvertisementsByCityAndCountryAndPostalCode(String city, String country, Integer postalCode) throws DataAccessException {
        return this.advertisementRepository.findByCityAndCountryAndPostalCode(city, country, postalCode);
    }

    @Transactional(readOnly = true)
    public Set<Advertisement> findAdvertisementsByCityWithPriceLessThan(String city, Double pricePerMonth) throws DataAccessException {
        return this.advertisementRepository.findByPricePerMonthLessThan(city, pricePerMonth);
    }

    @Transactional
    public void saveAdvertisement(Advertisement advertisement) throws DataAccessException {
        this.advertisementRepository.save(advertisement);
    }

    @Transactional public void deleteAdvertisement(Advertisement advertisement) throws DataAccessException {
        this.advertisementRepository.delete(advertisement);
    }
}
