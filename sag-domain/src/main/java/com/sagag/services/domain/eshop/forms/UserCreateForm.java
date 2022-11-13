package com.sagag.services.domain.eshop.forms;

import lombok.Data;

import java.util.List;

@Data
public class UserCreateForm {

  private String email;

  private String password;

  private String passwordRepeated;

  private List<Integer> roles;

  private String passwordHash;

  private String username;

  private int age;

}
