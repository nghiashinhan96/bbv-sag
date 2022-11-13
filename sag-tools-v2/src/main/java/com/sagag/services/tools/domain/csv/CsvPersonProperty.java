package com.sagag.services.tools.domain.csv;

import com.opencsv.bean.CsvBindByName;
import com.sagag.services.tools.domain.source.SourcePersonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class CsvPersonProperty implements Serializable {

  private static final long serialVersionUID = 373945794901849714L;

  @CsvBindByName(column = "PERSON_ID", locale = "US")
  private Long personId;

  @CsvBindByName(column = "TYPE")
  private String type;

  @CsvBindByName(column = "VALUE")
  private String value;

  public static CsvPersonProperty of(SourcePersonProperty entity) {
    CsvPersonProperty csvObj = new CsvPersonProperty();
    csvObj.setPersonId(entity.getSourcePersonPropertyId().getPersonId());
    csvObj.setType(entity.getSourcePersonPropertyId().getType());
    csvObj.setValue(entity.getValue());
    return csvObj;
  }

}
