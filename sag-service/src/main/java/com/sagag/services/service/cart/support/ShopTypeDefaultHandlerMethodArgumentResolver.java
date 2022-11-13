package com.sagag.services.service.cart.support;

import com.sagag.eshop.repo.hz.entity.ShopType;
import com.sagag.services.common.aspect.LogExecutionTime;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
public class ShopTypeDefaultHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

  public static final String DEFAULT_SHOP_TYPE_PARAMETER = "shopType";

  private static final ShopType DEFAULT_SHOP_TYPE = ShopType.DEFAULT_SHOPPING_CART;

  private String valueParameter = DEFAULT_SHOP_TYPE_PARAMETER;

  private ShopType fallbackShopType = DEFAULT_SHOP_TYPE;

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return ShopType.class.equals(parameter.getParameterType());
  }

  @Override
  @LogExecutionTime
  public ShopType resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
    log.debug("Resolving the shop type default argument");
    final String shopTypeValueFromRequest = webRequest.getParameter(valueParameter);
    if (StringUtils.isBlank(shopTypeValueFromRequest)) {
      log.debug("Shop type value from HTTP request is empty, we will use default value");
      return getDefaultFromAnnotationOrFallback(parameter);
    }
    log.debug("The shop type value from request = {}", shopTypeValueFromRequest);
    return ShopType.defaultValueOf(shopTypeValueFromRequest);
  }

  private ShopType getDefaultFromAnnotationOrFallback(MethodParameter methodParameter) {
    ShopTypeDefault defaults = methodParameter.getParameterAnnotation(ShopTypeDefault.class);
    if (defaults != null) {
      log.debug("The deafault value is = {}", defaults);
      return defaults.value();
    }
    return fallbackShopType;
  }

}
