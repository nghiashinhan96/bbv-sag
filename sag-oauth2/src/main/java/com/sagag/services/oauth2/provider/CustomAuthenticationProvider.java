package com.sagag.services.oauth2.provider;

import com.sagag.services.oauth2.model.user.EshopUserDetails;
import com.sagag.services.oauth2.processor.NonSaleAuthenticationProcessor;
import com.sagag.services.oauth2.processor.PasswordUserAuthenticationProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.function.Predicate;

@Component("authenticationProvider")
@Primary
public class CustomAuthenticationProvider implements AuthenticationProvider {

  private final UserDetailsService userService;

  private final PasswordEncoder passwordEncoder;

  private final NonSaleAuthenticationProcessor userAuthProcessor;

  private final PasswordUserAuthenticationProcessor passwordUserAuthProcessor;

  @Autowired
  public CustomAuthenticationProvider(final UserDetailsService userService,
      final PasswordEncoder passwordEncoder,
      final NonSaleAuthenticationProcessor userAuthProcessor,
      final PasswordUserAuthenticationProcessor passwordUserAuthProcessor) {
    this.userService = userService;
    this.passwordEncoder = passwordEncoder;
    this.userAuthProcessor = userAuthProcessor;
    this.passwordUserAuthProcessor = passwordUserAuthProcessor;
  }

  @Override
  public Authentication authenticate(final Authentication authentication) {
    final String username = authentication.getName();
    EshopUserDetails details = (EshopUserDetails) userService.loadUserByUsername(username);
    if (isNeedValidatePassword().test(details)) {
      final String password = authentication.getCredentials().toString();
      validatePassword(password, details.getEncodedPasswordAndSaltAsString());
      details = passwordUserAuthProcessor.process(details, password, details.getHashType());
    }
    if (isNeedReHandleAuthentication().test(details)) {
      details = userAuthProcessor.process(details);
    }

    final Collection<? extends GrantedAuthority> auths = details.getAuthorities();
    return new UsernamePasswordAuthenticationToken(details, "N/A", auths);
  }

  private static Predicate<EshopUserDetails> isNeedValidatePassword() {
    // no check password if the sales login on behalf of the customer
    return details -> !(details.isSaleOnBehalf() || details.isSso() || details.isAutonet()
        || details.isCloudDms());
  }

  private static Predicate<EshopUserDetails> isNeedReHandleAuthentication() {
    return details -> !(details.isSaleOnBehalf() || details.isSso() || details.isAutonet());
  }

  private void validatePassword(final String password, String storedPassword) {
    if (!passwordEncoder.matches(password, storedPassword)) {
      throw new BadCredentialsException("Invalid password.");
    }
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(UsernamePasswordAuthenticationToken.class);
  }
}
