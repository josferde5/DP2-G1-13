
package org.springframework.samples.flatbook.unit.web.formatters;

import java.text.ParseException;
import java.util.Locale;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.flatbook.model.Authorities;
import org.springframework.samples.flatbook.model.enums.AuthoritiesType;
import org.springframework.samples.flatbook.service.AuthoritiesService;
import org.springframework.samples.flatbook.web.formatters.AuthoritiesFormatter;

@ExtendWith(MockitoExtension.class)
class AuthoritiesFormatterTests {

	@Mock
	private AuthoritiesService		serviceService;

	private AuthoritiesFormatter	authoritiesFormatter;


	@BeforeEach
	void setup() {
		this.authoritiesFormatter = new AuthoritiesFormatter();
	}

	@Test
	void testPrint() {
		Authorities authority = new Authorities();
		authority.setAuthority(AuthoritiesType.TENANT);
		String authorityName = this.authoritiesFormatter.print(authority.getAuthority(), Locale.ENGLISH);
		Assertions.assertEquals("TENANT", authorityName);
	}

	@Test
	void shouldParse() throws ParseException {
		AuthoritiesType authority = this.authoritiesFormatter.parse("TENANT", Locale.ENGLISH);
		Assertions.assertEquals(AuthoritiesType.TENANT, authority);
	}

	@Test
	void shouldThrowException() throws ParseException {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.authoritiesFormatter.parse("TNT", Locale.ENGLISH);
		});
	}

}
