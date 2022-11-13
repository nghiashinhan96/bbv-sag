package com.sagag.services.stakis.erp.translator;

import static com.sagag.services.common.utils.XmlUtils.getValueOpt;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.sagag.services.common.profiles.CzProfile;
import com.sagag.services.common.translator.IDataTranslator;
import com.sagag.services.domain.sag.external.ContactInfo;
import com.sagag.services.stakis.erp.enums.StakisContactType;
import com.sagag.services.stakis.erp.wsdl.cis.ArrayOfContact;

@Component
@CzProfile
public class CisContactTranslator
  implements IDataTranslator<ArrayOfContact, Map<StakisContactType, List<ContactInfo>>> {

  @Override
  public Map<StakisContactType, List<ContactInfo>> translateToConnect(ArrayOfContact arrayOfContact) {
    if (arrayOfContact == null) {
      return Collections.emptyMap();
    }
    final List<ContactInfo> contacts = arrayOfContact.getContact().stream()
        .filter(aOfContact -> !Objects.isNull(aOfContact)
            && !Objects.isNull(aOfContact.getElectronicAddresses()))
        .map(aOfContact -> aOfContact.getElectronicAddresses().getValue())
        .filter(Objects::nonNull)
        .flatMap(eAddresses -> ListUtils.emptyIfNull(eAddresses.getElectronicAddress()).stream())
        .map(eAddress -> {
          final ContactInfo contact = new ContactInfo();
          contact.setType(contactTypeParser().apply(eAddress.getType()));
          getValueOpt(eAddress.getValue()).ifPresent(contact::setValue);
          getValueOpt(eAddress.getDescription()).ifPresent(contact::setDescription);
          contact.setPrimary(true);
          return contact;
        }).collect(Collectors.toList());

    final Map<StakisContactType, List<ContactInfo>> contactMap =
        new EnumMap<>(StakisContactType.class);
    contactMap.put(StakisContactType.PHONE,
        contactExtractor().apply(contacts, StakisContactType.PHONE));
    contactMap.put(StakisContactType.EMAIL,
        contactExtractor().apply(contacts, StakisContactType.EMAIL));
    contactMap.put(StakisContactType.FAX,
        contactExtractor().apply(contacts, StakisContactType.FAX));
    return contactMap;
  }

  private static Function<Integer, String> contactTypeParser() {
    return cisContactType -> StakisContactType.fromType(cisContactType).map(StakisContactType::name)
        .orElse(StringUtils.EMPTY);
  }

  private static BiFunction<List<ContactInfo>, StakisContactType,
    List<ContactInfo>> contactExtractor() {
    return (originalContacts, contactType) -> originalContacts.stream()
        .filter(contact -> contactType.name().equals(contact.getType()))
        .collect(Collectors.toList());
  }
}
