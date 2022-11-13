package com.sagag.services.tools.domain.csv;

import com.opencsv.bean.CsvBindByName;
import com.sagag.services.tools.domain.SourceBaseObject;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CsvOrganisation extends SourceBaseObject {

  private static final long serialVersionUID = -5157071435294426363L;

  @CsvBindByName(column = "ID", locale = "US")
  private Long id;

  @CsvBindByName(column = "ERP_NUMBER", locale = "US")
  private Long erpNumber;

  @CsvBindByName(column = "ERP_INSTANCE")
  private String erpInstance;

  @CsvBindByName(column = "TYPE")
  private String type;

  @CsvBindByName(column = "ORGANISATION_ID")
  private String organisationId;

  @CsvBindByName(column = "NAME")
  private String name;

  @CsvBindByName(column = "STATUS")
  private String status;

  @CsvBindByName(column = "DISTRIBUTIONSET", locale = "US")
  private Long distributionSet;

}
