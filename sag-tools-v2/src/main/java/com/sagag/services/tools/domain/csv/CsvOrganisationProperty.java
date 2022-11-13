package com.sagag.services.tools.domain.csv;

import com.opencsv.bean.CsvBindByName;
import com.sagag.services.tools.domain.source.SourceOrganisationProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class CsvOrganisationProperty implements Serializable {

  private static final long serialVersionUID = 373945794901849714L;

  @CsvBindByName(column = "ORGANISATION_ID", locale = "US")
  private Long organisationId;

  @CsvBindByName(column = "TYPE")
  private String type;

  @CsvBindByName(column = "VALUE")
  private String value;

  public static CsvOrganisationProperty of(final SourceOrganisationProperty entity) {
    CsvOrganisationProperty csvObj = new CsvOrganisationProperty();
    csvObj.setOrganisationId(entity.getSourceOrganisationPropertyId().getOrganisationId());
    csvObj.setType(entity.getSourceOrganisationPropertyId().getType());
    csvObj.setValue(entity.getValue());
    return csvObj;
  }

}
