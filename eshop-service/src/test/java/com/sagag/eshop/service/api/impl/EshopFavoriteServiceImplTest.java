package com.sagag.eshop.service.api.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sagag.eshop.repo.api.EshopFavoriteRepository;
import com.sagag.eshop.repo.entity.EshopFavorite;
import com.sagag.eshop.repo.enums.EshopFavoriteType;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.exception.EshopFavoriteException;
import com.sagag.eshop.service.validator.criteria.EshopFavoriteValidateCriteria;
import com.sagag.eshop.service.validator.favorite.EshopFavoriteValidator;
import com.sagag.eshop.service.validator.favorite.impl.ArticleFavoriteTypeValidator;
import com.sagag.eshop.service.validator.favorite.impl.LeafNodeFavoriteTypeValidator;
import com.sagag.eshop.service.validator.favorite.impl.VehicleFavoriteTypeValidator;
import com.sagag.services.common.exception.ValidationException;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.VehicleDto;
import com.sagag.services.domain.eshop.uniparts_favorite.dto.EshopFavoriteDto;

import lombok.Getter;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * UT for {@link EshopFavoriteServiceImpl}.
 */
@RunWith(SpringRunner.class)
public class EshopFavoriteServiceImplTest {

  private static final String TITLE = "TITLE ARTICLE";

  @InjectMocks
  private EshopFavoriteServiceImpl eshopFavoriteService = new EshopFavoriteServiceImpl();

  @Mock
  private EshopFavoriteRepository eshopFavoriteRepository;

  @Mock
  private LeafNodeFavoriteTypeValidator leafValidator;

  @Mock
  private ArticleFavoriteTypeValidator articleValidator;

  @Mock
  private VehicleFavoriteTypeValidator vehicleValidator;

  @Spy
  private List<EshopFavoriteValidator> favoriteTypeProcessors;

  @Mock
  @Getter
  private UserInfo user;

  /**
   * Sets up the pre-condition for testing.
   *
   * @throws Exception throws when program fails.
   */
  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    when(favoriteTypeProcessors.stream()).thenReturn(Stream.of(leafValidator, articleValidator, vehicleValidator));
    when(leafValidator.supportMode(EshopFavoriteType.LEAF_NODE)).thenReturn(true);
    when(articleValidator.supportMode(EshopFavoriteType.ARTICLE)).thenReturn(true);
    when(vehicleValidator.supportMode(EshopFavoriteType.VEHICLE)).thenReturn(true);
    when(user.getId()).thenReturn(123L);
  }

  @Test
  public void testAddFavoriteItemArticleSuccessFully() throws ValidationException {

    EshopFavoriteDto body = new EshopFavoriteDto();

    body.setType(EshopFavoriteType.ARTICLE.name());
    body.setArticleId("ARTICLE_123");
    body.setComment("ARTICLE FAVORITE");
    body.setTitle("TITLE VEHICLE");

    when(articleValidator.validate(EshopFavoriteValidateCriteria.builder().add(true)
        .userId(user.getOriginalUserId()).favoriteItem(body).build())).thenReturn(true);

    eshopFavoriteService.processFavoriteItem(user, body, true);
    verify(articleValidator, times(1)).validate((Mockito.any()));
    verify(eshopFavoriteRepository, times(1)).save(Mockito.any());
  }

  @Test
  public void testAddFavoriteItemVehicleSuccessFully() throws ValidationException {
    EshopFavoriteDto body = new EshopFavoriteDto();

    body.setType(EshopFavoriteType.VEHICLE.name());
    body.setArticleId("VEHICLE_123");
    body.setComment("VEHICLE FAVORITE");
    body.setTitle(TITLE);

    when(vehicleValidator.validate(EshopFavoriteValidateCriteria.builder().add(true)
        .userId(user.getOriginalUserId()).favoriteItem(body).build())).thenReturn(true);

    eshopFavoriteService.processFavoriteItem(user, body, true);
    verify(vehicleValidator, times(1)).validate((Mockito.any()));
    verify(eshopFavoriteRepository, times(1)).save(Mockito.any());
  }

  @Test
  public void testDeleteFavoriteItemSuccessFully() throws ValidationException {

    EshopFavoriteDto body = new EshopFavoriteDto();
    EshopFavorite itemExpected = new EshopFavorite();

    body.setType(EshopFavoriteType.ARTICLE.name());
    body.setArticleId("ARTICLE_123");
    body.setComment("ARTICLE FAVORITE");
    body.setTitle(TITLE);

    when(articleValidator.validate(EshopFavoriteValidateCriteria.builder().add(true)
        .userId(user.getId()).favoriteItem(body).build())).thenReturn(true);
    when(articleValidator.getFavoriteItemValid()).thenReturn(Optional.ofNullable(itemExpected));

    eshopFavoriteService.processFavoriteItem(user, body, false);

    verify(articleValidator, times(1)).validate(Mockito.any());
    verify(eshopFavoriteRepository, times(1)).delete(Mockito.any());
  }

  private static EshopFavoriteDto buildEshopFavoriteDto() {
    EshopFavoriteDto body = new EshopFavoriteDto();

    body.setType(EshopFavoriteType.LEAF_NODE.name());
    body.setTreeId("TREE_123");
    body.setLeafId("LEAF_123");
    body.setGaId("GA_123");
    body.setComment("LEAF NODE FAVORITE");
    body.setTitle("TITLE LEAF NODE");
    return body;
  }

  @Test
  public void testAddFavoriteItemLeafNodeSuccessFully() throws ValidationException {

    EshopFavoriteDto body = buildEshopFavoriteDto();

    when(leafValidator.validate(EshopFavoriteValidateCriteria.builder().add(true)
        .userId(user.getId()).favoriteItem(body).build())).thenReturn(true);

    eshopFavoriteService.processFavoriteItem(user, body, true);
    verify(leafValidator, times(1)).validate(Mockito.any());
    verify(eshopFavoriteRepository, times(1)).save(Mockito.any());
  }


  @Test(expected = EshopFavoriteException.class)
  public void testAddFavoriteItemShouldThrowEshopFavoriteExceptionGivenExistedItem()
      throws ValidationException {
    EshopFavoriteDto body = buildEshopFavoriteDto();

    when(leafValidator.validate(any())).thenThrow(EshopFavoriteException.class);

    eshopFavoriteService.processFavoriteItem(user, body, true);
    verify(eshopFavoriteRepository, times(0)).save(Mockito.any());
  }

  @Test(expected = EshopFavoriteException.class)
  public void testDeleteFavoriteIteShouldUnipartsFavoriteExceptionGivenNotExistedItem()
      throws ValidationException {

    EshopFavoriteDto body = buildEshopFavoriteDto();

    when(leafValidator.validate(any())).thenThrow(EshopFavoriteException.class);

    eshopFavoriteService.processFavoriteItem(user, body, false);
    verify(leafValidator, times(1)).validate(any());
    verify(eshopFavoriteRepository, times(0)).delete(any());
  }

  @Test
  public void getFavoriteItemListShouldReturnResultGivenUserInfo() {
    EshopFavorite itemExpected = new EshopFavorite();
    itemExpected.setUserId(user.getId());

    List<EshopFavorite> items = new ArrayList<>();
    items.add(itemExpected);

    when(eshopFavoriteRepository.findByUserIdOrderByCreatedTimeDesc(Mockito.anyLong()))
        .thenReturn(items);

    List<EshopFavoriteDto> result = eshopFavoriteService.getFavoriteItemList(user);

    Assert.assertThat(result.size(), Matchers.is(1));
    Assert.assertEquals(result.get(0).getUserId(), user.getId());
  }

  @Test
  public void updateFavoriteItemLeafNodeSuccessFully() throws ValidationException {
    EshopFavoriteDto bodyReq = new EshopFavoriteDto();
    EshopFavorite itemExpected = new EshopFavorite();

    itemExpected.setTitle("ARTICLE");

    bodyReq.setType(EshopFavoriteType.ARTICLE.name());
    bodyReq.setArticleId("ARTICLE 123");
    bodyReq.setTitle(TITLE);

    when(articleValidator.validate(EshopFavoriteValidateCriteria.builder().add(false)
        .userId(user.getId()).favoriteItem(bodyReq).build()))
    .thenReturn(true);

    when(articleValidator.getFavoriteItemValid()).thenReturn(Optional.ofNullable(itemExpected));
    when(eshopFavoriteRepository.save(itemExpected)).thenReturn(itemExpected);

    EshopFavoriteDto result = eshopFavoriteService.updateFavoriteItem(user, bodyReq);

    verify(eshopFavoriteRepository, times(1)).save(Mockito.any());
    Assert.assertEquals(result.getTitle(), bodyReq.getTitle());
  }


  @Test(expected = EshopFavoriteException.class)
  public void updateFavoriteItemShouldUnipartsFavoriteExceptionGivenNotExistedItem()
      throws ValidationException {
    EshopFavoriteDto bodyReq = new EshopFavoriteDto();
    bodyReq.setType(EshopFavoriteType.ARTICLE.name());
    bodyReq.setArticleId("ARTICLE 123");
    bodyReq.setTitle(TITLE);

    EshopFavorite favorite = new EshopFavorite();
    favorite.setComment("Comment");
    favorite.setComment("Title");

    when(articleValidator.validate(EshopFavoriteValidateCriteria.builder().add(false)
        .userId(user.getId()).favoriteItem(bodyReq).build()))
            .thenThrow(EshopFavoriteException.class);

    eshopFavoriteService.updateFavoriteItem(user, bodyReq);

    verify(eshopFavoriteRepository, times(0)).save(Mockito.any());
  }

  @Test
  public void updateFavoriteFlagArticles() {
    ArticleDocDto art1 = new ArticleDocDto();
    ArticleDocDto art2 = new ArticleDocDto();
    ArticleDocDto art3 = new ArticleDocDto();
    art1.setArtid("a1");
    art2.setArtid("a2");
    art3.setArtid("a3");

    EshopFavorite f1 = new EshopFavorite();
    EshopFavorite f2 = new EshopFavorite();
    f1.setArticleId("a1");
    f2.setArticleId("a3");
    f1.setComment("a");
    f2.setComment("b");

    List<ArticleDocDto> articleDocDtos = Arrays.asList(art1, art2, art3);
    List<EshopFavorite> uniFavorites = Arrays.asList(f1, f2);

    when(eshopFavoriteRepository.findFavoriteItemsByArticleIds(anyLong(), any()))
        .thenReturn(uniFavorites);

    eshopFavoriteService.updateFavoriteFlagArticles(user, articleDocDtos);

    Assert.assertTrue(art1.isFavorite());
    Assert.assertFalse(art2.isFavorite());
    Assert.assertTrue(art3.isFavorite());
  }
  

  @Test
  public void updateFavoriteFlagVehicles() {
    VehicleDto v1 = new VehicleDto();
    VehicleDto v2 = new VehicleDto();
    VehicleDto v3 = new VehicleDto();
    v1.setId("v1");
    v2.setId("v2");
    v3.setId("v3");

    EshopFavorite f1 = new EshopFavorite();
    EshopFavorite f2 = new EshopFavorite();
    f1.setVehicleId("v1");
    f2.setVehicleId("v3");
    f1.setComment("a");
    f2.setComment("b");

    List<VehicleDto> vehicleDtos = Arrays.asList(v1, v2, v3);
    List<EshopFavorite> uniFavorites = Arrays.asList(f1, f2);

    when(eshopFavoriteRepository.findFavoriteItemsByVehicleIds(anyLong(), any()))
        .thenReturn(uniFavorites);

    eshopFavoriteService.updateFavoriteFlagVehicle(user, vehicleDtos);

    Assert.assertTrue(v1.isFavorite());
    Assert.assertFalse(v2.isFavorite());
    Assert.assertTrue(v3.isFavorite());
  }


  @Test
  public void testSearchComments() {
    String keySearch = "a";
    final String comment1 = "Comment1";
    List<String> comments = Arrays.asList(comment1, "Comment2");
    when(eshopFavoriteRepository.findLatestUniqueComments(any(), any(), any()))
    .thenReturn(comments);

    List<String> result = eshopFavoriteService.searchComments(keySearch, user);

    Assert.assertTrue(result.stream().anyMatch(comment -> StringUtils.equals(comment1, comment)));
    Assert.assertThat(result.size(), Matchers.is(2));
  }
}
