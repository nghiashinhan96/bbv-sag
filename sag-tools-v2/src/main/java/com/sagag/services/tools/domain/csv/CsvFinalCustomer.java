package com.sagag.services.tools.domain.csv;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import com.sagag.services.tools.domain.source.SourceOrganisation;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

@Data
public class CsvFinalCustomer implements Serializable {

  private static final long serialVersionUID = -4179025985092219135L;

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

  @CsvBindByName(column = "UC_ID", locale = "US")
  private Long userCreatedId;

  @CsvBindByName(column = "DC", locale = "US")
  @CsvDate("yyyy-MM-dd HH:mm:ss")
  private Date dateCreated;

  @CsvBindByName(column = "UM_ID", locale = "US")
  private Long userModifiedId;

  @CsvBindByName(column = "DM", locale = "US")
  @CsvDate("yyyy-MM-dd HH:mm:ss")
  private Date dateModified;

  @CsvBindByName(column = "VERSION", locale = "US")
  private Integer version;

  @CsvBindByName(column = "TECSTATE")
  private String tecstate;

  @CsvBindByName(column = "COUNTRY_ISO")
  private String countryIso;

  public static CsvFinalCustomer of(SourceOrganisation source) {
    CsvFinalCustomer dest = new CsvFinalCustomer();
    BeanUtils.copyProperties(source, dest);
    return dest;
  }

}
