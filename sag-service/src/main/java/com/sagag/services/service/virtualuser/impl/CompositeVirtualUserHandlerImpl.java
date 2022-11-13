package com.sagag.services.service.virtualuser.impl;

import com.sagag.eshop.repo.entity.VVirtualUser;
import com.sagag.eshop.service.virtualuser.VirtualUserHandler;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
@Primary
@Slf4j
public class CompositeVirtualUserHandlerImpl implements VirtualUserHandler {

  @Autowired
  private List<VirtualUserHandler> virtualUserHandlers;

  @Override
  @Transactional(rollbackFor = Exception.class, timeout = 60) // 1 minute each execution
  public void accept(List<VVirtualUser> virtualUsers) {
    final Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.HOUR, -1);
    Date oneHourBack = calendar.getTime();
    final List<VVirtualUser> filteredVirtualUsers =
      userLoginBeforeInputHourBackExtractor(oneHourBack).apply(virtualUsers);
    virtualUserHandlers.stream()
      .peek(handler -> log.info("Running processor = {}", handler.toString()))
      .forEach(handler -> handler.accept(filteredVirtualUsers));
  }
}
