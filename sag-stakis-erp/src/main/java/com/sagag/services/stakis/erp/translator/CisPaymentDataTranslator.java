package com.sagag.services.stakis.erp.translator;

import java.util.Map.Entry;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.sagag.services.common.enums.PaymentMethodType;
import com.sagag.services.common.profiles.CzProfile;
import com.sagag.services.common.translator.IDataTranslator;
import com.sagag.services.stakis.erp.config.StakisErpProperties;
import com.sagag.services.stakis.erp.wsdl.cis.ArrayOfNote;

@Component
@CzProfile
public class CisPaymentDataTranslator implements IDataTranslator<ArrayOfNote, PaymentMethodType> {

  public static final String METHOD_OF_PAYMENT_TXT = "Method of payment";

  @Autowired
  private StakisErpProperties props;

  @Override
  public PaymentMethodType translateToConnect(ArrayOfNote arrayOfNote) {
    Assert.notNull(arrayOfNote, "The given array of note must not be null");
    final String noteStr = ListUtils.emptyIfNull(arrayOfNote.getNote()).stream()
        .filter(note -> StringUtils.equalsIgnoreCase(METHOD_OF_PAYMENT_TXT,
            note.getDescription().getValue()))
        .findFirst().map(eNote -> eNote.getText().getValue()).orElse(StringUtils.EMPTY);
    Assert.hasText(noteStr, "The given note string of payment method must not be empty");

    final Entry<PaymentMethodType, String> entry = props.getConfig().getPayment().entrySet()
        .stream().filter(e -> StringUtils.equalsIgnoreCase(noteStr, e.getValue()))
        .findFirst().orElseThrow(() -> new IllegalArgumentException());
    return entry.getKey();
  }

}
