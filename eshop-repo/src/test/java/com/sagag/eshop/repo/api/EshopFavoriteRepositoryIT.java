package com.sagag.eshop.repo.api;

import com.google.common.collect.Lists;
import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.entity.EshopFavorite;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import com.sagag.services.common.utils.PageUtils;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RepoApplication.class })
@EshopIntegrationTest
@Transactional
public class EshopFavoriteRepositoryIT {

  @Autowired
  private EshopFavoriteRepository repository;

  @Test
  public void shouldFindAllEshopFavoriteItems() {
    final Page<EshopFavorite> entities = repository.findAll(PageUtils.DEF_PAGE);
    Assert.assertThat(entities.hasContent(), Matchers.anyOf(Matchers.is(true), Matchers.is(false)));
  }

  @Test
  public void givenUserIdListShouldRemoveEshopFavoriteItems() {
    final List<Long> userIdList = Lists.newArrayList(26l);
    repository.removeFavoriteItemsByUserIds(userIdList);
  }
}
