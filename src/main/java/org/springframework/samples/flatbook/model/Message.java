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

package org.springframework.samples.flatbook.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

/**
 * Simple business object representing a pet.
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 */
@Entity
@Getter
@Setter
@Table(name = "messages")
public class Message extends BaseEntity implements Comparable<Message> {

	@Column(name = "creation_moment")
	@NotNull
	private LocalDateTime	creationMoment;

	@Column(name = "body")
	@NotBlank
	private String			body;

	@NotNull
	@ManyToOne(fetch = FetchType.EAGER)
	private Person			sender;

	@NotNull
	@ManyToOne(fetch = FetchType.EAGER)
	private Person			receiver;


	@Override
	public int compareTo(final Message o) {
		return this.creationMoment.compareTo(o.creationMoment);
	}

}
