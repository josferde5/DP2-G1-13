
package org.springframework.samples.flatbook.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "addresses")
public class Address extends BaseEntity {

	@Column(name = "address")
	@NotBlank
	private String	address;

	@Column(name = "city")
	@NotBlank
	private String	city;

	@Column(name = "postal_code")
	@NotNull
    @Size(min = 3, max = 9)
	private String	postalCode;

	@Column(name = "country")
	@NotBlank
	private String	country;
}
