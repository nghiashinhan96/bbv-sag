package com.sagag.services.stakis.erp.translator;

import org.springframework.stereotype.Component;

import com.sagag.services.common.enums.ErpInvoiceTypeCode;
import com.sagag.services.common.profiles.CzProfile;
import com.sagag.services.common.translator.IDataTranslator;
import com.sagag.services.stakis.erp.wsdl.cis.CustomerItem;

@Component
@CzProfile
public class CisInvoiceTypeTranslator implements IDataTranslator<CustomerItem, ErpInvoiceTypeCode> {

  @Override
  public ErpInvoiceTypeCode translateToConnect(CustomerItem invoiceMethodCustomerItem) {
    return ErpInvoiceTypeCode.SINGLE_INVOICE;
  }

}
