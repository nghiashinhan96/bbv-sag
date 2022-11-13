package com.sagag.services.hazelcast.user_context.impl;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.domain.eshop.dto.ContextDto;
import com.sagag.services.hazelcast.domain.user.EshopContext;
import com.sagag.services.hazelcast.domain.user.EshopContextType;
import com.sagag.services.hazelcast.user_context.IContextMapper;

import org.springframework.stereotype.Component;

@Component
public class UserPriceContextMapperImpl implements IContextMapper {

  @Override
  public void map(UserInfo user, EshopContext context, ContextDto contextDto) {
    context.updateUserPriceContext(contextDto.getUserPriceOptionContext());
  }

  @Override
  public EshopContextType type() {
    return EshopContextType.USER_PRICE_CONTEXT;
  }

}
