package com.sagag.services.service.cart.callable;

import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.hazelcast.api.CartManagerService;
import com.sagag.services.hazelcast.domain.cart.CartItemDtoBuilders;
import com.sagag.services.hazelcast.domain.cart.CartKeyGenerators;
import com.sagag.services.hazelcast.request.ShoppingCartRequestBody;
import com.sagag.services.service.DataProvider;
import com.sagag.services.service.utils.ArticleDocDtoDataTestProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Calendar;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RunWith(MockitoJUnitRunner.class)
public class CreateCartItemCallableCreatorImplTest {

  @InjectMocks
  private CreateCartItemCallableCreatorImpl createCartItemCallableCreator;

  @Mock
  private CartManagerService cartManagerService;

  @Test
  public void givenCartRequestShouldCreate() throws InterruptedException, ExecutionException {
    final ShopType shopType = ShopType.DEFAULT_SHOPPING_CART;
    ShoppingCartRequestBody cartRequest = new ShoppingCartRequestBody();
    ArticleDocDto article = ArticleDocDtoDataTestProvider.createArticleDoc();
    cartRequest.setArticle(article);
    cartRequest.setQuantity(2);
    UserInfo user = DataProvider.createUserInfo();
    final String cartItemKey = CartKeyGenerators.cartKey(user.key(), "", article.getIdSagsys(),
        article.getSupplierId(), article.getSupplierArticleNumber(), shopType);
    Mockito.when(cartManagerService.findByKey(user.key(), cartItemKey)).thenReturn(
        Optional.of(CartItemDtoBuilders.build(cartItemKey, user, cartRequest, shopType)));

    final ServletRequestAttributes mainThreadAttribute = null;
    Callable<Void> createCartItemCallable = createCartItemCallableCreator.create(cartRequest,
        mainThreadAttribute, user, Calendar.getInstance().getTime(), shopType);
    ExecutorService service = Executors.newSingleThreadExecutor();
    service.submit(createCartItemCallable).get();

    Mockito.verify(cartManagerService).findByKey(user.key(), cartItemKey);
  }
}
