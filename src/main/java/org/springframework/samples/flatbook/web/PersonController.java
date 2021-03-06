
package org.springframework.samples.flatbook.web;

import java.security.Principal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.flatbook.model.Authorities;
import org.springframework.samples.flatbook.model.Tenant;
import org.springframework.samples.flatbook.model.TenantReview;
import org.springframework.samples.flatbook.model.User;
import org.springframework.samples.flatbook.model.dtos.PersonForm;
import org.springframework.samples.flatbook.model.enums.AuthoritiesType;
import org.springframework.samples.flatbook.model.enums.SaveType;
import org.springframework.samples.flatbook.service.AdvertisementService;
import org.springframework.samples.flatbook.service.AuthoritiesService;
import org.springframework.samples.flatbook.service.HostService;
import org.springframework.samples.flatbook.service.PersonService;
import org.springframework.samples.flatbook.service.TenantService;
import org.springframework.samples.flatbook.service.UserService;
import org.springframework.samples.flatbook.service.apis.MailjetAPIService;
import org.springframework.samples.flatbook.service.exceptions.BadRequestException;
import org.springframework.samples.flatbook.service.exceptions.DuplicatedDniException;
import org.springframework.samples.flatbook.service.exceptions.DuplicatedEmailException;
import org.springframework.samples.flatbook.service.exceptions.DuplicatedUsernameException;
import org.springframework.samples.flatbook.utils.ReviewUtils;
import org.springframework.samples.flatbook.web.validators.PasswordValidator;
import org.springframework.samples.flatbook.web.validators.PersonAuthorityValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.common.collect.Lists;

@Controller
public class PersonController {

	private static final String	BANNED								= "banned";
	private static final String	ACTIVE								= "active";
	private static final String	USER_PAGE							= "users/usersPage";
	private static final String	USER_LIST							= "users/usersList";
	private static final String	ONLY_CAN_EDIT_YOUR_OWN_PROFILE		= "Only can edit your own profile";
	private static final String	ONLY_CAN_EDIT_YOUR_OWN_PASSWORD		= "Only can edit your own password";
	private static final String	USERS_UPDATE_PASSWORD				= "users/updatePassword";
	private static final String	USERS_CREATE_OR_UPDATE_USER_FORM	= "users/createOrUpdateUserForm";
	public static final String	USERNAME_DUPLICATED					= "username in use";
	public static final String	DNI_DUPLICATED						= "dni in use";
	public static final String	EMAIL_DUPLICATED					= "email in use";
	public static final String	WRONG_PASSWORD						= "wrong password";
	public static final String	PERSON_FORM							= "personForm";

	PersonService				personService;
	AuthoritiesService			authoritiesService;
	TenantService				tenantService;
	HostService					hostService;
	AdvertisementService		advertisementService;
	UserService					userService;
	MailjetAPIService			mailjetAPIService;


	@Autowired
	public PersonController(final PersonService personService, final AuthoritiesService authoritiesService, final TenantService tenantService,
		final AdvertisementService advertisementService, final UserService userService, final MailjetAPIService mailjetAPIService,
		final HostService hostService) {
		super();
		this.personService = personService;
		this.authoritiesService = authoritiesService;
		this.tenantService = tenantService;
		this.advertisementService = advertisementService;
		this.userService = userService;
		this.mailjetAPIService = mailjetAPIService;
		this.hostService = hostService;
	}

	@ModelAttribute("types")
	public List<AuthoritiesType> getTypes(final ModelMap model) {
		return Arrays.asList(AuthoritiesType.HOST, AuthoritiesType.TENANT);
	}

	@InitBinder("personForm")
	public void initBinder(final WebDataBinder dataBinder, final HttpServletRequest http) {
		if (http.getRequestURI().split("[/]")[http.getRequestURI().split("[/]").length - 1].equals("editPassword")) {
			dataBinder.setValidator(new PasswordValidator());
		} else {
			dataBinder.addValidators(new PasswordValidator());
			dataBinder.addValidators(new PersonAuthorityValidator());
		}
	}

	@GetMapping("/users/new")
	public String newUser(final ModelMap model) {
		PersonForm person = new PersonForm();
		person.setSaveType(SaveType.NEW);
		model.put(PersonController.PERSON_FORM, person);
		return PersonController.USERS_CREATE_OR_UPDATE_USER_FORM;
	}

	@PostMapping("/users/new")
	public String registerUser(final ModelMap model, @Valid final PersonForm user, final BindingResult result) {
		if (result.hasErrors()) {
			return PersonController.USERS_CREATE_OR_UPDATE_USER_FORM;
		} else {
			try {
				user.setSaveType(SaveType.NEW);
				this.personService.saveUser(user);
				this.mailjetAPIService.sendSimpleMessage(user.getFirstName() + " " + user.getLastName(), user.getEmail(), user.getUsername(),
					user.getPassword());
				return "redirect:/login";
			} catch (DuplicatedUsernameException e) {
				result.rejectValue("username", PersonController.USERNAME_DUPLICATED, PersonController.USERNAME_DUPLICATED);
				return PersonController.USERS_CREATE_OR_UPDATE_USER_FORM;
			} catch (DuplicatedDniException e) {
				result.rejectValue("dni", PersonController.DNI_DUPLICATED, PersonController.DNI_DUPLICATED);
				return PersonController.USERS_CREATE_OR_UPDATE_USER_FORM;
			} catch (DuplicatedEmailException e) {
				result.rejectValue("email", PersonController.EMAIL_DUPLICATED, PersonController.EMAIL_DUPLICATED);
				return PersonController.USERS_CREATE_OR_UPDATE_USER_FORM;
			}
		}
	}

	@GetMapping("/users/{username}/edit")
	public String findUserProfile(final ModelMap model, @PathVariable("username") final String username, final Principal principal) {
		if (username.equals(principal.getName())) {
			PersonForm user = new PersonForm(this.personService.findUserById(username));
			user.setAuthority(this.authoritiesService.findAuthorityById(username));
			user.setSaveType(SaveType.EDIT);
			model.put(PersonController.PERSON_FORM, user);
			return PersonController.USERS_CREATE_OR_UPDATE_USER_FORM;
		} else {
			throw new BadRequestException(PersonController.ONLY_CAN_EDIT_YOUR_OWN_PROFILE);
		}

	}

	@PostMapping("/users/{username}/edit")
	public String updateUserProfile(final ModelMap model, @Valid final PersonForm user, final BindingResult result,
		@PathVariable("username") final String username, final Principal principal)
		throws DuplicatedUsernameException, DuplicatedDniException, DuplicatedEmailException {
		if (result.hasErrors()) {
			return PersonController.USERS_CREATE_OR_UPDATE_USER_FORM;
		} else if (!username.equals(principal.getName())) {
			throw new BadRequestException(PersonController.ONLY_CAN_EDIT_YOUR_OWN_PROFILE);
		} else {
			user.setSaveType(SaveType.EDIT);
			user.setUsername(username);
			user.setPassword(this.personService.findUserById(username).getPassword());
			user.setAuthority(this.authoritiesService.findAuthorityById(user.getUsername()));
			this.personService.saveUser(user);
			return "redirect:/";
		}
	}

	@GetMapping("/users/{username}/editPassword")
	public String initPassword(final ModelMap model, @PathVariable("username") final String username, final Principal principal) {
		if (username.equals(principal.getName())) {
			PersonForm user = new PersonForm(this.personService.findUserById(username));
			user.setAuthority(this.authoritiesService.findAuthorityById(username));
			user.setSaveType(SaveType.EDIT);
			model.put(PersonController.PERSON_FORM, user);
			return PersonController.USERS_UPDATE_PASSWORD;
		} else {
			throw new BadRequestException(PersonController.ONLY_CAN_EDIT_YOUR_OWN_PASSWORD);
		}
	}

	@PostMapping("/users/{username}/editPassword")
	public String updatePassword(final ModelMap model, @Valid final PersonForm user, final BindingResult result,
		@PathVariable("username") final String username, final Principal principal)
		throws DuplicatedUsernameException, DuplicatedDniException, DuplicatedEmailException {
		if (result.hasErrors()) {
			return PersonController.USERS_UPDATE_PASSWORD;
		} else if (!username.equals(principal.getName())) {
			throw new BadRequestException(PersonController.ONLY_CAN_EDIT_YOUR_OWN_PASSWORD);
		} else {
			PersonForm previous = new PersonForm(this.personService.findUserById(username));
			if (!previous.getPassword().equals(user.getPreviousPassword())) {
				result.rejectValue("previousPassword", PersonController.WRONG_PASSWORD, PersonController.WRONG_PASSWORD);
				return PersonController.USERS_UPDATE_PASSWORD;
			}
			previous.setPassword(user.getPassword());
			previous.setAuthority(this.authoritiesService.findAuthorityById(username));
			previous.setSaveType(SaveType.EDIT);
			this.personService.saveUser(previous);
			return "redirect:/";
		}
	}

	@GetMapping("/users/{username}")
	public String initUserPage(final ModelMap model, @PathVariable("username") final String username, final Principal principal) {
		User user = this.personService.findUserById(username);
		if (user != null && (user.isEnabled()
			|| !user.isEnabled() && this.authoritiesService.findAuthorityById(principal.getName()).equals(AuthoritiesType.ADMIN))) {
			model.put("username", username);
			model.put("enabled", user.isEnabled());

			if (this.authoritiesService.findAuthorityById(username).equals(AuthoritiesType.TENANT)) {
				Tenant tenant = this.tenantService.findTenantById(username);
				model.put("canCreateReview", principal != null && ReviewUtils.isAllowedToReviewATenant(principal.getName(), username,
					this.tenantService, this.authoritiesService, this.hostService));
				List<TenantReview> reviews = tenant.getReviews().stream().filter(x -> x.getCreator().isEnabled()).collect(Collectors.toList());
				reviews.sort(Comparator.comparing(TenantReview::getCreationDate).reversed());
				model.put("reviews", reviews);
				model.put("tenantId", username);
				model.put("myFlatId", tenant.getFlat() != null ? tenant.getFlat().getId() : null);
			} else {
				model.put("selections", user.isEnabled() ? this.advertisementService.findAdvertisementsByHost(username) : Lists.newArrayList());
			}
			return PersonController.USER_PAGE;
		} else if (user != null && !user.isEnabled()) {
			throw new BadRequestException("This user is banned");
		} else {
			throw new BadRequestException("This user does not exists");
		}
	}

	@GetMapping("/users/list")
	public String initUserList(final ModelMap model, final Principal principal, @RequestParam(name = "show", required = false) final String show) {
		List<User> users = this.personService.findAllUsers().stream().sorted(Comparator.comparing(User::getUsername)).collect(Collectors.toList());
		List<Authorities> authorities = this.authoritiesService.findAll().stream().sorted(Comparator.comparing(Authorities::getUsername))
			.collect(Collectors.toList());

		if (show != null && show.equals(PersonController.ACTIVE)) {
			users.removeIf(x -> !x.isEnabled());
		} else if (show != null && show.equals(PersonController.BANNED)) {
			users.removeIf(User::isEnabled);
		}

		authorities.removeIf(x -> !users.stream().map(User::getUsername).collect(Collectors.toList()).contains(x.getUsername()));
		model.put("users", users);
		model.put("authorities", authorities);
		return PersonController.USER_LIST;
	}

	@GetMapping({
		"/users/{username}/ban", "/users/{username}/unban"
	})
	public String banOrUnbanUser(final ModelMap model, @PathVariable("username") final String username, final Principal principal) {
		User user = this.personService.findUserById(username);
		if (user != null) {
			if (user.isEnabled()) {
				this.personService.banUser(username);
				user.setEnabled(false);
			} else {
				user.setEnabled(true);
			}
			this.userService.save(user);
			return "redirect:/users/{username}";
		} else {
			throw new BadRequestException("This user does not exists");
		}
	}

}
