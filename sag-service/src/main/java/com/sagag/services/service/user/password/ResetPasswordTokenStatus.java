package com.sagag.services.service.user.password;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResetPasswordTokenStatus {

  TOKEN_INVALID("invalidToken"), TOKEN_EXPIRED("expired"), TOKEN_VALID("valid");

  private String code;
}
