
package org.springframework.samples.flatbook.web;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.flatbook.model.Address;
import org.springframework.samples.flatbook.model.Advertisement;
import org.springframework.samples.flatbook.model.DBImage;
import org.springframework.samples.flatbook.model.Flat;
import org.springframework.samples.flatbook.model.FlatReview;
import org.springframework.samples.flatbook.model.Host;
import org.springframework.samples.flatbook.model.pojos.GeocodeResponse;
import org.springframework.samples.flatbook.service.AdvertisementService;
import org.springframework.samples.flatbook.service.AuthoritiesService;
import org.springframework.samples.flatbook.service.DBImageService;
import org.springframework.samples.flatbook.service.FlatService;
import org.springframework.samples.flatbook.service.HostService;
import org.springframework.samples.flatbook.service.PersonService;
import org.springframework.samples.flatbook.service.apis.GeocodeAPIService;
import org.springframework.samples.flatbook.service.exceptions.BadRequestException;
import org.springframework.samples.flatbook.utils.FlatUtils;
import org.springframework.samples.flatbook.utils.ReviewUtils;
import org.springframework.samples.flatbook.web.validators.FlatValidator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class FlatController {

	private static final String			VIEWS_FLATS_CREATE_OR_UPDATE_FORM	= "flats/createOrUpdateFlatForm";
	private static final String			EXCEPTION_MESSAGE					= "Illegal access";
	private static final String			IMAGES_FIELD						= "images";

	private final FlatService			flatService;
	private final AuthoritiesService	authoritiesService;
	private final DBImageService		dbImageService;
	private final PersonService			personService;
	private final HostService			hostService;
	private final AdvertisementService	advertisementService;
	private final GeocodeAPIService		geocodeAPIService;


	@Autowired
	public FlatController(final FlatService flatService, final DBImageService dbImageService, final PersonService personService,
		final HostService hostService, final AdvertisementService advertisementService, final GeocodeAPIService geocodeAPIService,
		final AuthoritiesService authoritiesService) {
		this.flatService = flatService;
		this.dbImageService = dbImageService;
		this.personService = personService;
		this.hostService = hostService;
		this.advertisementService = advertisementService;
		this.geocodeAPIService = geocodeAPIService;
		this.authoritiesService = authoritiesService;
	}

	@InitBinder
	public void setAllowedFields(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@InitBinder("flat")
	public void initBinder(final WebDataBinder dataBinder, final HttpServletRequest http) {
		if (http.getRequestURI().split("[/]")[http.getRequestURI().split("[/]").length - 1].equals("new")) {
			dataBinder.addValidators(new FlatValidator());
		}
	}

	@GetMapping(value = "/flats/new")
	public String initCreationForm(final Map<String, Object> model) {
		Flat flat = new Flat();
		model.put("flat", flat);
		return FlatController.VIEWS_FLATS_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/flats/new")
	public String processCreationForm(@Valid final Flat flat, final BindingResult result) throws UnsupportedEncodingException {
		if (result.hasErrors()) {
			return FlatController.VIEWS_FLATS_CREATE_OR_UPDATE_FORM;
		} else {
			if (flat.getImages().size() < 6) {
				result.rejectValue(FlatController.IMAGES_FIELD, "", "a minimum of 6 images is required.");
				return FlatController.VIEWS_FLATS_CREATE_OR_UPDATE_FORM;
			}
			Address address = flat.getAddress();
            String errorView = getLatitudeAndLongitudeOfAddress(address, result);
			if(errorView != null) {
			    return errorView;
            }
			flat.setAddress(address);
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Host host = (Host) this.personService.findUserById(((User) auth.getPrincipal()).getUsername());
			host.addFlat(flat);
			this.flatService.saveFlat(flat);

			return "redirect:/flats/" + flat.getId();
		}
	}

	@GetMapping(value = "/flats/{flatId}/edit")
	public String initUpdateForm(@PathVariable("flatId") final int flatId, final Map<String, Object> model) {
		if (!FlatUtils.validateUser(flatId, this.hostService, this.flatService)) {
			throw new BadRequestException(FlatController.EXCEPTION_MESSAGE);
		}
		Flat flat = this.flatService.findFlatByIdWithFullData(flatId);
		model.put("flat", flat);
		model.put(FlatController.IMAGES_FIELD, flat.getImages());
		return FlatController.VIEWS_FLATS_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/flats/{flatId}/edit")
	public String processUpdateForm(@Valid final Flat flat, final BindingResult result, @PathVariable("flatId") final int flatId,
		final Map<String, Object> model) throws UnsupportedEncodingException {
		if (result.hasErrors()) {
			return FlatController.VIEWS_FLATS_CREATE_OR_UPDATE_FORM;
		} else {
			if (!FlatUtils.validateUser(flatId, this.hostService, this.flatService)) {
				throw new BadRequestException(FlatController.EXCEPTION_MESSAGE);
			}
			Set<DBImage> newImages = flat.getImages().stream().filter(x -> !x.getFileType().equals("application/octet-stream"))
				.collect(Collectors.toSet());
			Flat oldFlat = this.flatService.findFlatById(flatId);
			if (oldFlat.getImages().size() + newImages.size() < 6) {
				result.rejectValue(FlatController.IMAGES_FIELD, "", "a minimum of 6 images is required.");
				return FlatController.VIEWS_FLATS_CREATE_OR_UPDATE_FORM;
			}
            Address address = flat.getAddress();
            String errorView = getLatitudeAndLongitudeOfAddress(address, result);
            if(errorView != null) {
                return errorView;
            }
            flat.setAddress(address);
			Set<DBImage> images = oldFlat.getImages();
			images.addAll(newImages);
			flat.setImages(images);
			flat.setId(flatId);
			this.flatService.saveFlat(flat);

			return "redirect:/flats/{flatId}";
		}
	}

	@GetMapping(value = "/flats/{flatId}/images/{imageId}/delete")
	public String processDeleteImage(@PathVariable("flatId") final int flatId, @PathVariable("imageId") final int imageId,
		final Map<String, Object> model) {
		if (!FlatUtils.validateUser(flatId, this.hostService, this.flatService)) {
			throw new BadRequestException(FlatController.EXCEPTION_MESSAGE);
		}
		Flat flat = this.flatService.findFlatById(flatId);
		if (flat.getImages().size() == 6) {
			BadRequestException e = new BadRequestException(FlatController.EXCEPTION_MESSAGE);
			model.put("exception", e);
			return "exception";
		} else {
			DBImage image = this.dbImageService.getImageById(imageId);
			flat.deleteImage(image);
			this.dbImageService.deleteImage(image);
			return "redirect:/flats/{flatId}/edit";
		}
	}

	@GetMapping(value = "/flats/{flatId}")
	public ModelAndView showFlat(@PathVariable("flatId") final int flatId, final Principal principal) {
		if (!FlatUtils.validateUser(flatId, this.hostService, this.flatService)) {
			throw new BadRequestException(FlatController.EXCEPTION_MESSAGE);
		}
		ModelAndView mav = new ModelAndView("flats/flatDetails");
		Flat flat = this.flatService.findFlatByIdWithFullData(flatId);
		mav.addObject(flat);

		Host host = this.hostService.findHostByFlatId(flat.getId());
		mav.addObject("host", host.getUsername());
		mav.addObject(FlatController.IMAGES_FIELD, flat.getImages());

		Boolean existAd = this.advertisementService.isAdvertisementWithFlatId(flat.getId());
		mav.addObject("existAd", existAd);

		List<FlatReview> reviews = new ArrayList<>(flat.getFlatReviews());
		reviews.removeIf(x -> !x.getCreator().isEnabled());
		reviews.sort(Comparator.comparing(FlatReview::getCreationDate).reversed());
		mav.addObject("reviews", reviews);
		mav.addObject("flatId", flat.getId());
		mav.addObject("canCreateReview",
			principal != null && ReviewUtils.isAllowedToReviewAFlat(principal.getName(), flat.getId(), this.flatService, this.authoritiesService));
		return mav;
	}

	@GetMapping(value = "/flats/list")
	public ModelAndView showFlatsOfHost() {
		ModelAndView mav = new ModelAndView("flats/flatsOfHost");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = ((User) auth.getPrincipal()).getUsername();
		List<Flat> flats = new ArrayList<>(this.flatService.findFlatByHostUsername(username));
        Map<Flat, Integer> flatAd = this.advertisementService.findAdvertisementsByHost(username).stream().collect(Collectors.toMap(Advertisement::getFlat, Advertisement::getId));
		List<Integer> advIds = flats.stream().map(x -> flatAd.getOrDefault(x, null)).collect(Collectors.toList());
		mav.addObject("flats", flats);
		mav.addObject("advIds", advIds);
		return mav;
	}

	@GetMapping(value = "/flats/{flatId}/delete")
	public String processDeleteFlat(@PathVariable("flatId") final int flatId) {
		if (!FlatUtils.validateUser(flatId, this.hostService, this.flatService)) {
			throw new BadRequestException(FlatController.EXCEPTION_MESSAGE);
		}
		Flat flat = this.flatService.findFlatById(flatId);
		this.flatService.deleteFlat(flat);

		return "redirect:/flats/list";
	}

	private String getLatitudeAndLongitudeOfAddress(Address address, BindingResult bindingResult) throws UnsupportedEncodingException {
        GeocodeResponse geocode = this.geocodeAPIService.getGeocodeData(address.getLocation() + ", " + address.getCity());
        if (geocode.getStatus().equals("ZERO_RESULTS")) {
            bindingResult.rejectValue("address.location", "", "The address does not exist. Try again.");
            return FlatController.VIEWS_FLATS_CREATE_OR_UPDATE_FORM;
        } else if (!geocode.getStatus().equals("OK")) {
            bindingResult.reject("An external error has occurred. Please try again later.");
            return FlatController.VIEWS_FLATS_CREATE_OR_UPDATE_FORM;
        }
        address.setLatitude(geocode.getResults().get(0).getGeometry().getLocation().getLat());
        address.setLongitude(geocode.getResults().get(0).getGeometry().getLocation().getLng());

        return null;
    }

}
