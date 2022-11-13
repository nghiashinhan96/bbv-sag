package com.sagag.services.tools.utils;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.sagag.services.tools.domain.ax.AxAddress;
import com.sagag.services.tools.domain.ax.AxContact;
import com.sagag.services.tools.support.CustomerAddressType;

import lombok.experimental.UtilityClass;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Ax address Utilities.
 *
 */
@UtilityClass
public final class AxAddressUtils {

  public static final String PHONE_CONTACT_TYPE = "Phone";

  public static final String EMAIL_CONTACT_TYPE = "Email";

  public static final String URL_CONTACT_TYPE = "URL";

  public static final String TELEX_CONTACT_TYPE = "Telex";

  public static final String FAX_CONTACT_TYPE = "Fax";

  private static final String[] CONTACT_TYPES = new String[] {
    PHONE_CONTACT_TYPE,
    EMAIL_CONTACT_TYPE,
    URL_CONTACT_TYPE,
    TELEX_CONTACT_TYPE,
    FAX_CONTACT_TYPE
  };

  public static Predicate<AxAddress> filterActiveDeliveryOrInvoiceAddress() {
    return address -> {
      if (CustomerAddressType.DEFAULT == address.getAddressType()) {
        return true;
      }
      return address.isActive();
    };
  }
  
  public static List<AxContact> findContactsByType(final List<AxContact> contacts,
      final String contactType) {
    if (CollectionUtils.isEmpty(contacts) || StringUtils.isBlank(contactType)
        || !ArrayUtils.contains(CONTACT_TYPES, contactType)) {
      return Collections.emptyList();
    }
    return contacts.stream()
        .filter(contact -> StringUtils.equalsIgnoreCase(contactType, contact.getContactType()))
        .collect(Collectors.toList());
  }

}
