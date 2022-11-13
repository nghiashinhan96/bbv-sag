package com.sagag.services.service.utils;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.utils.DateUtils;
import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.gtmotive.domain.response.GtmotiveSimpleOperation;
import com.sagag.services.ivds.response.GtmotiveResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

@Component
public class NormautoMailBuilder {

  @Value("${external.webservice.normautoAddress:}")
  private String normautoAddress;

  @Autowired
  private MessageSource messageSource;

  /**
   * The service support to build normauto mail content to send to client.
   *
   * @param user the authed user
   * @param criteria the gtmotive criteria
   * @param response the gtmotive response
   * @return a mail content string {@link String}
   *
   */
  public String build(final UserInfo user, final String vinCode, final String vehicleCode,
      final GtmotiveResponse response) {

    if (response == null || CollectionUtils.isEmpty(response.getNonMatchedOperations())) {
      return StringUtils.EMPTY;
    }

    // Build mail content
    final StringBuilder contentBuilder = new StringBuilder("mailto:");

    // Build normauto address
    contentBuilder.append(normautoAddress).append("?");

    final Locale locale = LocaleContextHolder.getLocale();
    final String companyName = StringUtils.defaultIfBlank(user.getCustomer().getCompanyName(), StringUtils.EMPTY);

    final String subject = getMessage("mail.offer_order.subject", Arrays.array(companyName), locale);
    contentBuilder.append("subject=").append(subject);

    // Build body text
    final StringBuilder bodyBuilder = new StringBuilder();

    // Build first last name
    final String fullName = user.getFirstName() + StringUtils.SPACE + user.getLastName();
    bodyBuilder.append(getMessage("mail.offer_order.make_offer_welcome", null, locale));
    bodyBuilder.append(System.lineSeparator()).append(System.lineSeparator());

    bodyBuilder
        .append(messageSource.getMessage("mail.offer_order.for", Arrays.array(fullName), locale));
    bodyBuilder.append(System.lineSeparator());

    // Build company name
    bodyBuilder.append(getMessage("mail.offer_order.from", Arrays.array(companyName), locale));
    bodyBuilder.append(System.lineSeparator());

    // Build user email
    bodyBuilder.append(getMessage("mail.offer_order.email", Arrays.array(user.getEmail()), locale));
    bodyBuilder.append(System.lineSeparator()).append(System.lineSeparator());

    // Build the list of part numbers
    bodyBuilder.append(getMessage("mail.offer_order.parts", null, locale));
    bodyBuilder.append(System.lineSeparator());

    bodyBuilder.append(getMessage("mail.offer_order.header.designation", null, locale));
    bodyBuilder.append(StringUtils.SPACE).append(StringUtils.SPACE).append(StringUtils.SPACE);
    bodyBuilder.append(StringUtils.SPACE).append(StringUtils.SPACE).append(StringUtils.SPACE);
    bodyBuilder.append(StringUtils.SPACE).append(StringUtils.SPACE).append(StringUtils.SPACE);
    bodyBuilder.append(getMessage("mail.offer_order.header.oe_number", null, locale));
    bodyBuilder.append(System.lineSeparator());

    final List<GtmotiveSimpleOperation> operations = response.getNonMatchedOperations();
    operations.forEach(operation -> bodyBuilder.append(operation.getPartDescription())
        .append(StringUtils.SPACE).append(StringUtils.SPACE).append(StringUtils.SPACE)
        .append(StringUtils.SPACE).append(operation.getReference()).append(System.lineSeparator()));

    // Build vehicle info
    bodyBuilder.append(System.lineSeparator());
    bodyBuilder.append(getMessage("mail.offer_order.vehicle", null, locale));
    bodyBuilder.append(System.lineSeparator());

    // Build vin info
    bodyBuilder.append(getMessage("mail.offer_order.vin",
        Arrays.array(StringUtils.defaultIfBlank(vinCode, StringUtils.EMPTY)), locale));
    bodyBuilder.append(System.lineSeparator());

    // Build typenschein info
    bodyBuilder.append(getMessage("mail.offer_order.typenschein",
        Arrays.array(StringUtils.defaultIfBlank(vehicleCode, StringUtils.EMPTY)), locale));
    bodyBuilder.append(System.lineSeparator());

    // Build vehicle make model type info
    String vehicleInfo = StringUtils.EMPTY;
    String vehId = StringUtils.EMPTY;
    final VehicleDto vehicle = response.getVehicle();
    if (vehicle != null) {
      vehId = StringUtils.defaultIfBlank(vehicle.getVehId(), StringUtils.EMPTY);
      vehicleInfo = StringUtils.defaultIfBlank(vehicle.getVehicleInfo(), StringUtils.EMPTY);
    }
    bodyBuilder.append(getMessage("mail.offer_order.vehicleId", Arrays.array(vehId), locale));
    bodyBuilder.append(System.lineSeparator());

    bodyBuilder.append(getMessage("mail.offer_order.type", Arrays.array(vehicleInfo), locale));
    bodyBuilder.append(System.lineSeparator()).append(System.lineSeparator());

    // Build user name
    bodyBuilder.append(getMessage("mail.offer_order.username",
        Arrays.array(user.getUsername()), locale));
    bodyBuilder.append(System.lineSeparator());

    // Build affiliate name info
    bodyBuilder.append(getMessage("mail.offer_order.client",
        Arrays.array(StringUtils.capitalize(user.getAffiliateShortName())), locale));
    bodyBuilder.append(System.lineSeparator());

    // Build customer number
    bodyBuilder.append(getMessage("mail.offer_order.customer_number",
        Arrays.array(user.getCustNrStr()), locale));
    bodyBuilder.append(System.lineSeparator()).append(System.lineSeparator());

    // Build time stamp
    bodyBuilder.append(DateUtils.toStringDate(Calendar.getInstance().getTime()));

    contentBuilder.append("&body=").append(StringUtils.replace(bodyBuilder.toString(),
        System.lineSeparator(), SagConstants.EMAIL_NEW_LINE_CHAR));

    return StringUtils.replace(contentBuilder.toString(), StringUtils.SPACE,
        SagConstants.EMAIL_SPACE_CHAR);
  }

  private String getMessage(String code, Object[] args, Locale locale) {
    return messageSource.getMessage(code, args, locale);
  }
}
