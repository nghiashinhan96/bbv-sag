package com.sagag.services.ivds.request;

import com.sagag.eshop.service.dto.UserInfo;

import lombok.Builder;
import lombok.Data;

import org.springframework.data.domain.PageRequest;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class UnitreeFreetextSearchRequest implements Serializable {

  private static final long serialVersionUID = -3742708529187452228L;

  private String text;

  private UserInfo user;

  private PageRequest pageRequest;

  /** the flag indicating whether to get all information for articles from ERP. */
  private boolean isFullRequest;

  private List<String> searchOptions;
}
