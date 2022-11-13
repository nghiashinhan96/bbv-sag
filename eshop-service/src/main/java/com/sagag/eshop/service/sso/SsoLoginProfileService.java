package com.sagag.eshop.service.sso;

import com.sagag.eshop.service.dto.sso.SsoLoginProfileRequestDto;
import com.sagag.eshop.service.dto.sso.SsoLoginProfileResponseDto;

public interface SsoLoginProfileService {

  /**
   * Returns login profile.
   * 
   * @param request
   * @return
   */
  SsoLoginProfileResponseDto createProfile(SsoLoginProfileRequestDto request);
}
