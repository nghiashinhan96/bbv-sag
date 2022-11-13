package com.sagag.services.dvse.api;

import com.sagag.services.dvse.dto.bonus.ValidateRecognitionResponse;

public interface HappyBonusValidateTokenService {

  /**
   * Validates happy bonus token.
   *
   * @param token
   * @return the response of {@link ValidateRecognitionResponse}
   */
  ValidateRecognitionResponse validateToken(String token);

}
