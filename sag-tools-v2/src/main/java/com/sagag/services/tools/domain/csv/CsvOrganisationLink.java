package com.sagag.services.tools.domain.csv;

import com.opencsv.bean.CsvBindByName;
import com.sagag.services.tools.domain.SourceBaseObject;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CsvOrganisationLink extends SourceBaseObject {

  private static final long serialVersionUID = -3730161763046755588L;

  @CsvBindByName(column = "ID", locale = "US")
  private Long id;

  @CsvBindByName(column = "VENDOR_ID", locale = "US")
  private Long vendorId;

  @CsvBindByName(column = "CLIENT_ID", locale = "US")
  private Long clientId;

  @CsvBindByName(column = "SALESORG_ID", locale = "US")
  private Long salesOrgId;

  @CsvBindByName(column = "TYPE", locale = "US")
  private String type;

}
