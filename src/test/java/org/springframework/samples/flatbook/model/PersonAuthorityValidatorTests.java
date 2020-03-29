
package org.springframework.samples.flatbook.model;

import java.util.Locale;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.samples.flatbook.model.enums.AuthoritiesType;
import org.springframework.samples.flatbook.model.mappers.PersonForm;
import org.springframework.samples.flatbook.util.TestUtils;
import org.springframework.samples.flatbook.web.validators.PersonAuthorityValidator;

public class PersonAuthorityValidatorTests {

	private PersonForm						personForm;

	private static PersonAuthorityValidator	personAuthorityValidator;


	@BeforeAll
	static void instaciateValidator() {
		PersonAuthorityValidatorTests.personAuthorityValidator = new PersonAuthorityValidator();
	}

	@BeforeEach
	void instanciatePersonForm() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		this.personForm = new PersonForm();
		this.personForm.setAuthority(AuthoritiesType.TENNANT);
	}

	@Test
	void shouldNotValidateWhenAthorityIsEmpty() {

		this.personForm.setAuthority(null);

		TestUtils.multipleAssert(PersonAuthorityValidatorTests.personAuthorityValidator, this.personForm, "authority->required");
	}

	@Test
	void shouldNotValidateWhenAthorityIsAdmin() {

		this.personForm.setAuthority(AuthoritiesType.ADMIN);

		TestUtils.multipleAssert(PersonAuthorityValidatorTests.personAuthorityValidator, this.personForm, "authority->cant be an admin");
	}

	@Test
	void shouldValidate() {
		TestUtils.multipleAssert(PersonAuthorityValidatorTests.personAuthorityValidator, this.personForm, (String[]) null);
	}
}