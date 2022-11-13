package com.sagag.eshop.repo.password;

import com.sagag.eshop.repo.entity.PasswordHash;
import com.sagag.services.common.affiliate.AffiliateConfigurationFactory;
import com.sagag.services.common.enums.HashType;
import com.sagag.services.common.security.CompositePasswordEncoder;
import com.sagag.services.common.security.PasswordHashBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class DefaultPasswordHashBuilder implements PasswordHashBuilder<PasswordHash> {

  @Autowired
  private AffiliateConfigurationFactory affiliateConfigFactory;

  @Autowired
  private CompositePasswordEncoder passwordEncoder;

  @Override
  public PasswordHash buildPasswordHash(String rawPassword, HashType hashType) {
    Assert.hasText(rawPassword, "The given raw password must not be empty");
    Assert.notNull(hashType, "The given hash type must not be null");
    return PasswordHash.ofPasswordAsString(passwordEncoder.encode(rawPassword), hashType);
  }

  @Override
  public PasswordHash buildPasswordHash(String rawPassword) {
    return buildPasswordHash(rawPassword, affiliateConfigFactory.getHashType());
  }

  @Override
  public PasswordHash buildPasswordHash(String passwordHash, String passwordSalt,
      HashType hashType) {
    Assert.hasText(passwordHash, "The given password hash must not be empty");
    Assert.hasText(passwordSalt, "The given password salt must not be empty");
    PasswordHash loginPasswordHash = new PasswordHash();
    loginPasswordHash.setHashType(hashType);
    loginPasswordHash.setPasswordHash(passwordHash);
    loginPasswordHash.setPasswordSalt(passwordSalt);
    return loginPasswordHash;
  }

}
