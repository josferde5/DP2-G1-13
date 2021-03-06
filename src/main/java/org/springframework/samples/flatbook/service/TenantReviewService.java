package org.springframework.samples.flatbook.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.flatbook.model.TenantReview;
import org.springframework.samples.flatbook.repository.TenantReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TenantReviewService {

    private TenantReviewRepository tenantReviewRepository;

    @Autowired
    public TenantReviewService(TenantReviewRepository tenantReviewRepository) {
        this.tenantReviewRepository = tenantReviewRepository;
    }

    @Transactional(readOnly = true)
    public TenantReview findTenantReviewById(int tenantReviewId) {
        return this.tenantReviewRepository.findById(tenantReviewId);
    }

    @Transactional
    public void saveTenantReview(TenantReview tenantReview) {
        this.tenantReviewRepository.save(tenantReview);
    }

    @Transactional
	public void deleteTenantReviewById(final int tenantReviewId) {
    	this.tenantReviewRepository.deleteById(tenantReviewId);
	}
}
