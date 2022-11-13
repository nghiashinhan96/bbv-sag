package com.sagag.services.gtmotive.domain.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@XmlType(name = "", propOrder = {
        "gsId", "gsPwd", "customerId", "userId"
})
@XmlRootElement
public class AuthenticationData {
  private String gsId;

  private String gsPwd;

  private String customerId;

  private String userId;


}
