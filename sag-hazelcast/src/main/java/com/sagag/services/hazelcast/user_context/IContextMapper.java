package com.sagag.services.hazelcast.user_context;

import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.domain.eshop.dto.ContextDto;
import com.sagag.services.hazelcast.domain.user.EshopContext;
import com.sagag.services.hazelcast.domain.user.EshopContextType;

public interface IContextMapper {

  void map(UserInfo user, EshopContext context, ContextDto contextDto);

  EshopContextType type();
}
