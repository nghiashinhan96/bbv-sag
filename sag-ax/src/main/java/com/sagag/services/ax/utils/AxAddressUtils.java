package com.sagag.services.ax.utils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.sagag.services.ax.domain.AxAddress;
import com.sagag.services.ax.domain.AxContact;
import com.sagag.services.ax.enums.CustomerAddressType;
import com.sagag.services.domain.sag.erp.Address;

import lombok.experimental.UtilityClass;

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

  public static String defaultEmailIfNotPresent(final List<AxContact> contacts,
    final String defaultValue) {
    return defaultContactValueIfNotPresent(contacts, EMAIL_CONTACT_TYPE, defaultValue);
  }

  public static String defaultPhoneIfNotPresent(final List<AxContact> contacts,
    final String defaultValue) {
    return defaultContactValueIfNotPresent(contacts, PHONE_CONTACT_TYPE, defaultValue);
  }

  public static String defaultContactValueIfNotPresent(final List<AxContact> contacts,
    final String contactType, final String defaultValue) {
    Optional<AxContact> contactOpt = findContactInfoByType(contacts, contactType);
    if (!contactOpt.isPresent()) {
      return defaultValue;
    }
    return contactOpt.get().getContactValue();
  }

  private static Optional<AxContact> findContactInfoByType(final List<AxContact> contacts,
    final String contactType) {
    List<AxContact> filteredContacts = findContactsByType(contacts, contactType);
    if (CollectionUtils.isEmpty(filteredContacts)) {
      return Optional.empty();
    }
    return filteredContacts.stream().findFirst();
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

  public static Optional<Address> findDefaultAddress(final List<Address> addresses) {
    if (CollectionUtils.isEmpty(addresses)) {
      return Optional.empty();
    }
    return findAddressByType(addresses, CustomerAddressType.DEFAULT);
  }

  public static Optional<Address> findDeliveryAddress(final List<Address> addresses) {
    if (CollectionUtils.isEmpty(addresses)) {
      return Optional.empty();
    }
    return findAddressByType(addresses, CustomerAddressType.DELIVERY);
  }

  public static Optional<Address> findInvoiceAddress(final List<Address> addresses) {
    if (CollectionUtils.isEmpty(addresses)) {
      return Optional.empty();
    }
    return findAddressByType(addresses, CustomerAddressType.INVOICE);
  }

  private static Optional<Address> findAddressByType(List<Address> customerAddresses,
      CustomerAddressType type) {
    if (CollectionUtils.isEmpty(customerAddresses) || Objects.isNull(type)) {
      return Optional.empty();
    }

    return customerAddresses.stream()
        .filter(address -> StringUtils.equalsIgnoreCase(type.name(), address.getAddressType()))
        .findFirst();
  }

  public static Predicate<AxAddress> filterActiveDeliveryOrInvoiceAddress() {
    return address -> {
      if (CustomerAddressType.DEFAULT == address.getAddressType()) {
        return true;
      }
      return address.isActive();
    };
  }
}
