package com.sagag.eshop.repo.api.message;

import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.entity.message.MessageHiding;
import com.sagag.services.common.annotation.EshopIntegrationTest;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Integration test class for role {@link MessageHiding}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@EshopIntegrationTest
@Transactional
public class MessageHidingRepositoryIT {

  @Autowired
  private MessageHidingRepository messageHidingRepo;

  @Test
  public void testFindNoAuthedMessagesOK() throws Exception {
    List<Long> messageIds = messageHidingRepo.findHidingMessagesByUser(13L);
    Assert.assertTrue(CollectionUtils.isNotEmpty(messageIds));
  }

}
