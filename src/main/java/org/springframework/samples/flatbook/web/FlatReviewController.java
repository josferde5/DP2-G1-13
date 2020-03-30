package org.springframework.samples.flatbook.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.flatbook.model.Flat;
import org.springframework.samples.flatbook.model.FlatReview;
import org.springframework.samples.flatbook.model.Tenant;
import org.springframework.samples.flatbook.service.FlatReviewService;
import org.springframework.samples.flatbook.service.FlatService;
import org.springframework.samples.flatbook.service.TenantService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Map;

@Controller
public class FlatReviewController {

    private static final String VIEWS_FLATREVIEWS_CREATE_OR_UPDATE_FORM = "flats/reviews/createOrUpdateFlatReviewForm";

    private final TenantService tenantService;
    private final FlatReviewService flatReviewService;
    private final FlatService flatService;

    @Autowired
    public FlatReviewController(FlatService flatService, FlatReviewService flatReviewService, TenantService tenantService) {
        this.flatReviewService = flatReviewService;
        this.tenantService = tenantService;
        this.flatService = flatService;
    }

    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    @GetMapping(value = "/flats/{flatId}/reviews/new")
    public String initCreationForm(Map<String, Object> model, Principal principal, @PathVariable("flatId") final Integer flatId) {
    	Tenant user = tenantService.findTenantById(principal.getName());
    	Flat flat = this.flatService.findFlatById(flatId);
    	if(user != null && flat != null && flat.getTenants().contains(user)) {
    		FlatReview fr = new FlatReview();
            fr.setCreator(user);
            fr.setCreationDate(LocalDate.now());
            model.put("flatReview", fr);
            return VIEWS_FLATREVIEWS_CREATE_OR_UPDATE_FORM;
    	}else {
    		throw new IllegalArgumentException("Bad flat id or you can not write a review of the flat.");
    	}
    }

    @PostMapping(value = "/flats/{flatId}/reviews/new")
    public String processCreationForm(@Valid FlatReview fr, BindingResult result, Principal principal, @PathVariable("flatId") final Integer flatId) {
    	Tenant user = tenantService.findTenantById(principal.getName());
    	Flat flat = this.flatService.findFlatById(flatId);
    	if(user != null && flat != null && flat.getTenants().contains(user)) {
    		if(result.hasErrors()) {
    			return VIEWS_FLATREVIEWS_CREATE_OR_UPDATE_FORM;
    		} else {
    			fr.setCreationDate(LocalDate.now());
        		flat.getFlatReviews().add(fr);
        		this.flatReviewService.saveFlatReview(fr);
        		this.flatService.saveFlat(flat);
        		return "redirect:/";
    		}
    	}else {
    		throw new IllegalArgumentException("Bad flat id or you can not write a review of the flat.");
    	}
    }

    @GetMapping(value = "/flats/{flatId}/reviews/{flatReviewId}/remove")
	public String processFlatReviewRemoval(@PathVariable("flatReviewId") final int flatReviewId, @PathVariable("flatId") final int flatId, Principal principal) {
    	FlatReview flatReview = this.flatReviewService.findFlatReviewById(flatReviewId);
    	Tenant creator = this.tenantService.findTenantById(principal.getName());
    	Flat reviewedFlat = this.flatService.findFlatById(flatId);
		if (creator != null && flatReview != null && reviewedFlat != null && creator.equals(flatReview.getCreator())) {
			reviewedFlat.getFlatReviews().remove(flatReview);
			this.flatReviewService.deleteFlatReviewById(flatReviewId);
			this.flatService.saveFlat(reviewedFlat);
			return "redirect:/";
		} else {
			throw new IllegalArgumentException("Bad flat review id or you are not the creator of the review.");
		}
	}
}
