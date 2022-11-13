package com.sagag.services.admin.exporter;

import com.sagag.services.common.exporter.IExportAttributes;
import com.sagag.services.common.profiles.AtSbProfile;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@AtSbProfile
@Component
public class AtUserExportAttributes implements IExportAttributes<BackOfficeUserExportAttributeType> {

  @Override
  public List<BackOfficeUserExportAttributeType> getExportAttributes() {
    return Arrays.asList(
        BackOfficeUserExportAttributeType.CUSTOMER_NUMBER,
        BackOfficeUserExportAttributeType.DVSE_CUSTOMER_NAME,
        BackOfficeUserExportAttributeType.DVSE_USERNAME,
        BackOfficeUserExportAttributeType.USERNAME,
        BackOfficeUserExportAttributeType.FIRST_NAME,
        BackOfficeUserExportAttributeType.LAST_NAME,
        BackOfficeUserExportAttributeType.EMAIL,
        BackOfficeUserExportAttributeType.ZIP,
        BackOfficeUserExportAttributeType.FIRST_LOGIN_DATE,
        BackOfficeUserExportAttributeType.LAST_LOGIN_DATE);
  }
}
