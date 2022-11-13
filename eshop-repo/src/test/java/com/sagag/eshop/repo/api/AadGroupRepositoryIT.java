package com.sagag.eshop.repo.api;

import static org.junit.Assert.assertNotNull;

import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.entity.AadGroup;
import com.sagag.services.common.annotation.EshopIntegrationTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@EshopIntegrationTest
@Transactional
public class AadGroupRepositoryIT {

  @Autowired
  private AadGroupRepository aadGroupRepository;

  @Test
  public void findAllByUuids_shouldReturnGroups_givenUuid() throws Exception {
    List<AadGroup> groups =
        aadGroupRepository.findAllByUuids(Arrays.asList("366a1f4a-380a-43c7-b3fe-322edb5cf491"))
            .orElse(Collections.emptyList());
    assertNotNull(groups);
  }
}
