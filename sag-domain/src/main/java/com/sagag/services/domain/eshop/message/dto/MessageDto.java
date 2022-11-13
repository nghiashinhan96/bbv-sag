package com.sagag.services.domain.eshop.message.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;

  private String content;

  /**
   * Banner - shown at the very top of the page on all pages except login. Panel - All pages unless
   * defined per shop, or Shopping Basket/order Confirmation. Note Login is a specific Panel.
   */
  private String type;

  /**
   * Which page the message is shown
   */
  private String area;

  /**
   * Which part in the page the message is shown
   */
  private String subArea;

  private int sort;

  private String style;

  private String visibility;

  private String locationType;

  private boolean ssoTraining;

  public MessageDto(String content) {
    this.content = content;
  }
}
