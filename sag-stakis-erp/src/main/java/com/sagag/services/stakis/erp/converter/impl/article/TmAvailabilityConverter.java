package com.sagag.services.stakis.erp.converter.impl.article;

import static com.sagag.services.common.utils.XmlUtils.getValueOpt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import com.sagag.services.common.profiles.CzProfile;
import com.sagag.services.domain.sag.erp.Availability;
import com.sagag.services.stakis.erp.enums.StakisArticleAvailabilityState;
import com.sagag.services.stakis.erp.wsdl.tmconnect.ArrayOfQuantity;
import com.sagag.services.stakis.erp.wsdl.tmconnect.ArrayOfWarehouse;
import com.sagag.services.stakis.erp.wsdl.tmconnect.AvailabilityState;
import com.sagag.services.stakis.erp.wsdl.tmconnect.EntityLink;
import com.sagag.services.stakis.erp.wsdl.tmconnect.ErpInformation;
import com.sagag.services.stakis.erp.wsdl.tmconnect.Quantity;
import com.sagag.services.stakis.erp.wsdl.tmconnect.Warehouse;

@Component
@CzProfile
public class TmAvailabilityConverter implements InitializingBean {

  private Map<String, String> arrivalTimePatternsByLanguage;

  @Override
  public void afterPropertiesSet() throws Exception {
    this.arrivalTimePatternsByLanguage = new HashMap<>();
    this.arrivalTimePatternsByLanguage.putIfAbsent("cs",
        "dd. MMMM yyyy HH:mm:ss");
    this.arrivalTimePatternsByLanguage.putIfAbsent(Locale.GERMAN.getLanguage(),
        "EEEE, dd. MMMM yyyy HH:mm:ss");
    this.arrivalTimePatternsByLanguage.putIfAbsent(Locale.ITALIAN.getLanguage(),
        "EEEE dd MMMM yyyy HH.mm.ss");
    this.arrivalTimePatternsByLanguage.putIfAbsent(Locale.ENGLISH.getLanguage(),
        "EEEE, MMMM dd, yyyy KK:mm:ss aa");
  }

  public List<Availability> apply(Optional<ErpInformation> erpInfoOpt,
      Map<String, AvailabilityState> states, String language) {
    if (!erpInfoOpt.isPresent() || MapUtils.isEmpty(states)) {
      return Collections.emptyList();
    }

    final ErpInformation erpInfor = erpInfoOpt.get();
    final List<String> uuidListOfAvailState = new ArrayList<>();
    getValueOpt(erpInfor.getAvailabilityState()).map(EntityLink::getGuid)
    .ifPresent(uuidListOfAvailState::add);

    final List<Warehouse> wareHouseList = getValueOpt(erpInfor.getWarehouses())
        .map(ArrayOfWarehouse::getWarehouse).orElse(Collections.emptyList());

    final List<Quantity> quantities = new ArrayList<>();
    wareHouseList.forEach(w -> getValueOpt(w.getQuantities())
        .map(ArrayOfQuantity::getQuantity)
        .ifPresent(qList -> {
          quantities.addAll(qList);
          qList.forEach(q -> getValueOpt(q.getAvailabilityState())
              .ifPresent(link -> uuidListOfAvailState.add(link.getGuid())));
        }));

    return uuidListOfAvailState.stream()
        .map(uuid -> {
          final AvailabilityState state = states.get(uuid);
          final Quantity quantity = findQuantityOfAvailState(quantities, uuid);
          if (quantity == null) {
            return availabilityConverter().apply(state, null);
          }

          final Integer quantityVal = quantity.getValue().intValue();
          final Availability availability = availabilityConverter().apply(state, quantityVal);
          getValueOpt(quantity.getExpectedDeliveryTime())
          .ifPresent(availability::setRawArrivalTime);
          if (!StringUtils.isBlank(availability.getRawArrivalTime())) {
            availability.setArrivalTime(arrivalTimeConverter(language)
                .apply(availability.getRawArrivalTime()));
          }
          return availability;
        }).collect(Collectors.toList());
  }

  private static Quantity findQuantityOfAvailState(List<Quantity> quantities, String uuid) {
    for (Quantity q : quantities) {
      if (getValueOpt(q.getAvailabilityState())
          .filter(e -> e.getGuid().equalsIgnoreCase(uuid)).isPresent()) {
        return q;
      }
    }
    return null;
  }

  private static BiFunction<AvailabilityState, Integer, Availability> availabilityConverter() {
    return (availState, quantity) -> {
      StakisArticleAvailabilityState state =
          StakisArticleAvailabilityState.fromCode(availState.getType());
      final Availability erpArtAvail = new Availability();
      erpArtAvail.setAvailState(state.getCode());
      erpArtAvail.setAvailStateColor(state.name());
      erpArtAvail.setQuantity(quantity);
      return erpArtAvail;
    };
  }

  private Function<String, String> arrivalTimeConverter(String language) {
    return rawArrivalTime -> DateTimeFormat.forPattern(defaultPatternByLang(language))
        .withLocale(new Locale(language))
        .parseDateTime(rawArrivalTime).toString();
  }

  private String defaultPatternByLang(String lang) {
    return Optional.ofNullable(this.arrivalTimePatternsByLanguage.get(lang))
    .orElseGet(() -> this.arrivalTimePatternsByLanguage.get(Locale.ENGLISH.getLanguage()));
  }

}
