package com.sagag.services.tools.domain.csv;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import com.sagag.services.tools.domain.source.SourcePerson;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

@Data
public class CsvPerson implements Serializable {

  private static final long serialVersionUID = 2300665033582559142L;

  @CsvBindByName(column = "ID", locale = "US")
  private Long id;

  @CsvBindByName(column = "TYPE")
  private String type;

  @CsvBindByName(column = "STATUS")
  private String status;

  @CsvBindByName(column = "FIRSTNAME")
  private String firstName;

  @CsvBindByName(column = "LASTNAME")
  private String lastName;

  @CsvBindByName(column = "EMAIL")
  private String email;

  @CsvBindByName(column = "LANGISO")
  private String langIso;

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

  public static CsvPerson of(SourcePerson entity) {
    CsvPerson person = new CsvPerson();
    BeanUtils.copyProperties(entity, person);
    return person;
  }

}
