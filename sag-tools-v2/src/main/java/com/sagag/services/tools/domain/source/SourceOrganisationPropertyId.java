package com.sagag.services.tools.domain.source;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
public class SourceOrganisationPropertyId implements Serializable {

	private static final long serialVersionUID = -4475101279668341673L;

	@Column(name = "ORGANISATION_ID")
	private Long organisationId;

	@Column(name = "TYPE")
	private String type;
}
