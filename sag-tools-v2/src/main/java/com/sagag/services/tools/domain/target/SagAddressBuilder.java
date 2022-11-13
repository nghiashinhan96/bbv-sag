package com.sagag.services.tools.domain.target;

import com.sagag.services.tools.support.SagConstants;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SagAddressBuilder {

  private String companyName;

  private String street;

  private String postCode;

  private String city;

  private String country;

  private String separator;

  public SagAddressBuilder companyName(String companyName) {
    this.companyName = companyName;
    return this;
  }

  public SagAddressBuilder street(String street) {
    this.street = street;
    return this;
  }

  public SagAddressBuilder postCode(String postCode) {
    this.postCode = postCode;
    return this;
  }

  public SagAddressBuilder city(String city) {
    this.city = city;
    return this;
  }

  public SagAddressBuilder country(String country) {
    this.country = country;
    return this;
  }

  public SagAddressBuilder separator(String separator) {
    this.separator = separator;
    return this;
  }

  public String build() {
    if (StringUtils.isBlank(this.separator)) {
      separator = SagConstants.COMMA;
    }
    final List<String> addrItems =
        Stream.of(companyName, street, postCode, city, country)
        .filter(StringUtils::isNotBlank).collect(Collectors.toList());
    return StringUtils.join(addrItems, separator);
  }
}
