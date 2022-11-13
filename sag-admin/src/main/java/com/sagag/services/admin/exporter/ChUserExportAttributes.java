package com.sagag.services.admin.exporter;

import com.sagag.services.common.exporter.IExportAttributes;
import com.sagag.services.common.profiles.ChProfile;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;

@ChProfile
@Component
public class ChUserExportAttributes implements IExportAttributes<BackOfficeUserExportAttributeType> {

  @Override
  public List<BackOfficeUserExportAttributeType> getExportAttributes() {
    return Arrays.asList(
        BackOfficeUserExportAttributeType.CUSTOMER_NUMBER,
        BackOfficeUserExportAttributeType.DVSE_CUSTOMER_NAME,
        BackOfficeUserExportAttributeType.DVSE_USERNAME,
        BackOfficeUserExportAttributeType.AFFILIATE_SHORTNAME,
        BackOfficeUserExportAttributeType.ROLE_NAME,
        BackOfficeUserExportAttributeType.SALUTAION,
        BackOfficeUserExportAttributeType.LANGUAGE,
        BackOfficeUserExportAttributeType.USERNAME,
        BackOfficeUserExportAttributeType.FIRST_NAME,
        BackOfficeUserExportAttributeType.LAST_NAME,
        BackOfficeUserExportAttributeType.EMAIL,
        BackOfficeUserExportAttributeType.CH_ZIP,
        BackOfficeUserExportAttributeType.CH_FIRST_LOGIN_DATE,
        BackOfficeUserExportAttributeType.CH_LAST_LOGIN_DATE);
  }
}
