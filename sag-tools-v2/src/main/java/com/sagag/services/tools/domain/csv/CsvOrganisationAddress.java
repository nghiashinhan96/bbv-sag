package com.sagag.services.tools.domain.csv;

import com.opencsv.bean.CsvBindByName;
import com.sagag.services.tools.domain.SourceBaseObject;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CsvOrganisationAddress extends SourceBaseObject {

  private static final long serialVersionUID = 7782342796510963701L;

  @CsvBindByName(column = "ORGANISATION_ID", locale = "US")
  private Long organisationId;

  @CsvBindByName(column = "ADDRESS_ID", locale = "US")
  private Long addressId;

}
