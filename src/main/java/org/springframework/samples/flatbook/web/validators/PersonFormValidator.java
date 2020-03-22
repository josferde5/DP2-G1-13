/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.flatbook.web.validators;

import java.util.regex.Pattern;

import org.springframework.samples.flatbook.model.mappers.PersonForm;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class PersonFormValidator implements Validator {

	private static final String	PASSWORD_PATTERN		= "must have 2 uppers, 2 lowers, 2 simbols, 2 numbers and 8 characters min";
	private static final String	PASSWORDS_DOESNT_MATCH	= "password doesnt match";
	private static final String	REQUIRED				= "required";


	@Override
	public void validate(final Object obj, final Errors errors) {
		PersonForm personForm = (PersonForm) obj;

		if (personForm.getPassword() != null) {
			if (personForm.getPassword() == "") {
				errors.rejectValue("password", PersonFormValidator.REQUIRED, PersonFormValidator.REQUIRED);
			} else {
				String pattern = "^(?=(.*[0-9]){2})(?=(.*[!-\\.<-@_]){2})(?=(.*[A-Z]){2})(?=(.*[a-z]){2})\\S{8,100}$";
				boolean matches = Pattern.matches(pattern, personForm.getPassword());

				if (!matches) {
					errors.rejectValue("password", PersonFormValidator.PASSWORD_PATTERN, PersonFormValidator.PASSWORD_PATTERN);
				} else if (!personForm.getConfirmPassword().equals(personForm.getPassword())) {
					errors.rejectValue("confirmPassword", PersonFormValidator.PASSWORDS_DOESNT_MATCH, PersonFormValidator.PASSWORDS_DOESNT_MATCH);
				}
			}
		}

	}

	@Override
	public boolean supports(final Class<?> clazz) {
		return PersonForm.class.isAssignableFrom(clazz);
	}

}