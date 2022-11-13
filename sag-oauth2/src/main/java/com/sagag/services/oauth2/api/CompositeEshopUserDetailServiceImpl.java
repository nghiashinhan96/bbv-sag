package com.sagag.services.oauth2.api;

import com.sagag.services.oauth2.api.impl.user.AbstractEshopUserDetailsService;
import com.sagag.services.oauth2.helper.EshopAuthHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class CompositeEshopUserDetailServiceImpl implements UserDetailsService {

  private final List<AbstractEshopUserDetailsService> userDetailServices;

  private final EshopAuthHelper eshopAuthHelper;

  @Autowired
  public CompositeEshopUserDetailServiceImpl(
    List<AbstractEshopUserDetailsService> usrDetailServices, EshopAuthHelper eshopAuthHelper) {
    this.userDetailServices = usrDetailServices;
    this.eshopAuthHelper = eshopAuthHelper;
  }

  @Override
  public UserDetails loadUserByUsername(String username) {
    final String affiliate = eshopAuthHelper.getLoginAffiliate();
    final AbstractEshopUserDetailsService service = userDetailServices.stream()
        .filter(item -> item.support(affiliate))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Not found correct mode"));
    return service.loadUserByUsername(username);
  }

}
