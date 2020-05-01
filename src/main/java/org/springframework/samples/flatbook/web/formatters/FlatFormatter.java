
package org.springframework.samples.flatbook.web.formatters;

import java.text.ParseException;
import java.util.Collection;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.samples.flatbook.model.Flat;
import org.springframework.samples.flatbook.service.FlatService;
import org.springframework.stereotype.Component;

@Component
public class FlatFormatter implements Formatter<Flat> {

	private FlatService flatService;


	@Autowired
	public FlatFormatter(final FlatService flatService) {
		this.flatService = flatService;
	}

	@Override
	public Flat parse(final String idText, final Locale locale) throws ParseException {
		Collection<Flat> flats = this.flatService.findAllFlats();
		Integer id = Integer.valueOf(idText);
		for (Flat f : flats) {
			if (f.getId().equals(id)) {
				return f;
			}
		}
		throw new ParseException("Flat not found with id " + id, 0);
	}

	@Override
	public String print(final Flat object, final Locale locale) {
		return object.getId().toString();
	}
}
