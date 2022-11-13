package com.sagag.eshop.service.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Eshop permission Dto class.
 */
@Data
@Builder
public class PermissionDto implements Serializable {

  private static final long serialVersionUID = 4417706836891101343L;

  private int id;

  private String description;

  private String permission;

  private Long createdBy; // the person id who created this permission

  private Long modifiedBy; // the person id who modified this permission

  private List<FunctionDto> functions;

}
