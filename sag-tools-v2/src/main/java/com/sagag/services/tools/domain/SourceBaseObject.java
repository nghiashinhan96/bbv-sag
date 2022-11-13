package com.sagag.services.tools.domain;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Date;

@Data
@MappedSuperclass
public class SourceBaseObject implements Serializable {

  private static final long serialVersionUID = -4301348528849902974L;

  @Column(name = "UC_ID")
  @CsvBindByName(column = "UC_ID", locale = "US")
  private Long userCreatedId;

  @Column(name = "DC")
  @CsvBindByName(column = "DC", locale = "US")
  @CsvDate("yyyy-MM-dd HH:mm:ss")
  private Date dateCreated;

  @Column(name = "UM_ID")
  @CsvBindByName(column = "UM_ID", locale = "US")
  private Long userModifiedId;

  @Column(name = "DM")
  @CsvBindByName(column = "DM", locale = "US")
  @CsvDate("yyyy-MM-dd HH:mm:ss")
  private Date dateModified;

  @Column(name = "VERSION")
  @CsvBindByName(column = "VERSION", locale = "US")
  private Integer version;

  @Column(name = "TECSTATE")
  @CsvBindByName(column = "TECSTATE")
  private String tecstate;

}
