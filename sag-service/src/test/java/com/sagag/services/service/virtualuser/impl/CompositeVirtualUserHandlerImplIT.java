package com.sagag.services.service.virtualuser.impl;

import com.sagag.eshop.repo.api.VVirtualUserRepository;
import com.sagag.eshop.repo.entity.VVirtualUser;
import com.sagag.eshop.service.virtualuser.VirtualUserHandler;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.utils.PageUtils;
import com.sagag.services.service.SagServiceApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { SagServiceApplication.class })
@EshopIntegrationTest
@Transactional
public class CompositeVirtualUserHandlerImplIT {

  @Autowired
  private VirtualUserHandler virtualUserHandler;

  @Autowired
  private VVirtualUserRepository vVirtualUserRepo;

  @Test
  public void testHandler() {
    final Pageable pageable = PageUtils.defaultPageable(10);
    final Page<VVirtualUser> virtualUsersPage = vVirtualUserRepo.findVirtualUsers(pageable);
    virtualUserHandler.accept(virtualUsersPage.getContent());
  }

}
