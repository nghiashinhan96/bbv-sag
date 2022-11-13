package com.sagag.services.service.order.steps;

import static org.hamcrest.CoreMatchers.is;

import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.sag.erp.Availability;
import com.sagag.services.hazelcast.domain.cart.ShoppingCartItem;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class ShoppingCartItemAvailabilityTest {

  @Test
  public void isAvailabilityShoppingCartItem_from_AxAndVen() {
    ShoppingCartItem items = new ShoppingCartItem();

    ArticleDocDto article1 = new ArticleDocDto();
    article1.setArtid("1111111111");

    List<Availability> availabilities = new ArrayList<>();
    availabilities.add(createSimpleAxAvailability());
    availabilities.add(createSimpleVenAvailability());
    article1.setAvailabilities(availabilities);

    items.setArticle(article1);

    Assert.assertThat(items.isNotAvailable(), is(false));
    Assert.assertThat(items.isAvailable(), is(true));
  }

  @Test
  public void isAvailabilityShoppingCartItem_from_AxOnly() {
    ShoppingCartItem items = new ShoppingCartItem();

    ArticleDocDto article1 = new ArticleDocDto();
    article1.setArtid("1111111111");

    List<Availability> availabilities = new ArrayList<>();
    availabilities.add(createSimpleAxAvailability());
    article1.setAvailabilities(availabilities);

    items.setArticle(article1);

    Assert.assertThat(items.isNotAvailable(), is(false));
    Assert.assertThat(items.isAvailable(), is(true));
  }

  @Test
  public void isAvailabilityShoppingCartItem_from_VenOnly() {
    ShoppingCartItem items = new ShoppingCartItem();

    ArticleDocDto article1 = new ArticleDocDto();
    article1.setArtid("1111111111");

    List<Availability> availabilities = new ArrayList<>();
    availabilities.add(createSimpleAxAvailability());
    article1.setAvailabilities(availabilities);

    items.setArticle(article1);

    Assert.assertThat(items.isNotAvailable(), is(false));
    Assert.assertThat(items.isAvailable(), is(true));
  }

  @Test
  public void isNotAvailabilityShoppingCartItem_if_avail_has_Con_1() {
    ShoppingCartItem items = new ShoppingCartItem();

    ArticleDocDto article1 = new ArticleDocDto();
    article1.setArtid("1111111111");

    List<Availability> availabilities = new ArrayList<>();
    availabilities.add(createSimpleConAvailability());
    article1.setAvailabilities(availabilities);

    items.setArticle(article1);

    Assert.assertThat(items.isNotAvailable(), is(true));
    Assert.assertThat(items.isAvailable(), is(false));
  }

  @Test
  public void isNotAvailabilityShoppingCartItem_if_avail_has_Con_2() {
    ShoppingCartItem items = new ShoppingCartItem();

    ArticleDocDto article1 = new ArticleDocDto();
    article1.setArtid("1111111111");

    List<Availability> availabilities = new ArrayList<>();
    availabilities.add(createSimpleConAvailability());
    availabilities.add(createSimpleVenAvailability());
    article1.setAvailabilities(availabilities);

    items.setArticle(article1);

    Assert.assertThat(items.isNotAvailable(), is(true));
    Assert.assertThat(items.isAvailable(), is(false));
  }

  @Test
  public void isNotAvailabilityShoppingCartItem_if_avail_has_Con_3() {
    ShoppingCartItem items = new ShoppingCartItem();

    ArticleDocDto article1 = new ArticleDocDto();
    article1.setArtid("1111111111");

    List<Availability> availabilities = new ArrayList<>();
    availabilities.add(createSimpleConAvailability());
    availabilities.add(createSimpleAxAvailability());
    article1.setAvailabilities(availabilities);

    items.setArticle(article1);

    Assert.assertThat(items.isNotAvailable(), is(true));
    Assert.assertThat(items.isAvailable(), is(false));
  }

  @Test
  public void isNotAvailabilityShoppingCartItem_if_avail_has_Con_4() {
    ShoppingCartItem items = new ShoppingCartItem();

    ArticleDocDto article1 = new ArticleDocDto();
    article1.setArtid("1111111111");

    List<Availability> availabilities = new ArrayList<>();
    availabilities.add(createSimpleConAvailability());
    availabilities.add(createSimpleAxAvailability());
    availabilities.add(createSimpleVenAvailability());
    article1.setAvailabilities(availabilities);

    items.setArticle(article1);

    Assert.assertThat(items.isNotAvailable(), is(true));
    Assert.assertThat(items.isAvailable(), is(false));
  }

  @Test
  public void isNotAvailabilityShoppingCartItem_if_avail_isBackOrderTrue_1() {
    ShoppingCartItem items = new ShoppingCartItem();

    ArticleDocDto article1 = new ArticleDocDto();
    article1.setArtid("1111111111");

    List<Availability> availabilities = new ArrayList<>();
    availabilities.add(createSimpleBackOrderTrueAvailability());
    article1.setAvailabilities(availabilities);

    items.setArticle(article1);

    Assert.assertThat(items.isNotAvailable(), is(true));
    Assert.assertThat(items.isAvailable(), is(false));
  }

  @Test
  public void isNotAvailabilityShoppingCartItem_if_avail_isBackOrderTrue_2() {
    ShoppingCartItem items = new ShoppingCartItem();

    ArticleDocDto article1 = new ArticleDocDto();
    article1.setArtid("1111111111");

    List<Availability> availabilities = new ArrayList<>();
    availabilities.add(createSimpleBackOrderTrueAvailability());
    availabilities.add(createSimpleAxAvailability());
    article1.setAvailabilities(availabilities);

    items.setArticle(article1);

    Assert.assertThat(items.isNotAvailable(), is(true));
    Assert.assertThat(items.isAvailable(), is(false));
  }

  private Availability createSimpleBackOrderTrueAvailability() {
    return Availability.builder()
        .backOrder(true)
        .conExternalSource(false)
        .venExternalSource(false)
        .externalSource(false)
        .build();
  }

  private Availability createSimpleConAvailability() {
    return Availability.builder()
        .backOrder(false)
        .conExternalSource(true)
        .venExternalSource(false)
        .externalSource(true)
        .build();
  }

  private Availability createSimpleVenAvailability() {
   return Availability.builder()
        .backOrder(false)
        .conExternalSource(false)
        .venExternalSource(true)
        .externalSource(true)
        .build();
  }

  private Availability createSimpleAxAvailability() {
    return Availability.builder()
        .backOrder(false)
        .conExternalSource(false)
        .venExternalSource(false)
        .externalSource(false)
        .arrivalTime("2021-01-07T06:05:00Z")
        .build();
  }
}
