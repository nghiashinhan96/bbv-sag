package com.sagag.services.domain.eshop.forms;

import lombok.Data;

import java.util.List;

@Data
public class UserSearchForm {

  private String username;

  private String email;

  private List<Integer> roles;

  private Integer age;

}
