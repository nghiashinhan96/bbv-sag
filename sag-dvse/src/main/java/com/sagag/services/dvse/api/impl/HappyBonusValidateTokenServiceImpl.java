package com.sagag.services.dvse.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sagag.services.common.utils.XmlUtils;
import com.sagag.services.dvse.api.HappyBonusValidateTokenService;
import com.sagag.services.dvse.authenticator.bonus.TokenRecognitionAuthenticator;
import com.sagag.services.dvse.dto.bonus.ValidateRecognitionResponse;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class HappyBonusValidateTokenServiceImpl implements HappyBonusValidateTokenService {

  @Autowired
  private TokenRecognitionAuthenticator tokenRecognitionAuthenticator;

  @Override
  public ValidateRecognitionResponse validateToken(String token) {
    log.info("Validates Happy Bonus Token = {}", token);
    final ValidateRecognitionResponse response = new ValidateRecognitionResponse();
    response.setValidatedRecognition(tokenRecognitionAuthenticator.authenticate(token));
    log.info("Validated Happy Bonus Result = {}", XmlUtils.marshalWithPrettyMode(response));
    return response;
  }

}
