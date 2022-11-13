package com.sagag.services.oauth2.model.user;

import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.repo.entity.GroupUser;
import com.sagag.eshop.repo.entity.Login;
import com.sagag.eshop.repo.entity.PasswordHash;
import com.sagag.services.common.contants.SagConstants;
import com.sagag.services.common.enums.HashType;
import com.sagag.services.common.utils.HashUtils;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = false)
public class EshopUserDetails extends User {

  private static final long serialVersionUID = 1348543844898511600L;

  private final Long id;
  private final Long salesId;
  private final String locatedAffiliate;
  private final boolean sso;
  private final boolean autonet;
  private final boolean isCloudDms;

  private HashType hashType;

  private String passwordSalt;

  private String passwordHash;

  @Override
  public boolean isAccountNonExpired() {
    return isEnabled();
  }

  @Override
  public boolean isAccountNonLocked() {
    return isEnabled();
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return isEnabled();
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  /**
   * Constructs the current logging user instance.
   *
   * @param user the database user.
   */
  public EshopUserDetails(final EshopUser user, final Long salesId, final String locatedAffiliate,
      final boolean isSso, final boolean isAutonet, boolean isCloudDms) {
    super(buildUniqueUsername(user.getId(), salesId),
        user.getLogin().getNonNullPassword(),
        AuthorityUtils.createAuthorityList(findUserAuthorities(user)));
    this.id = user.getId();
    this.salesId = salesId;
    this.locatedAffiliate = locatedAffiliate;
    this.sso = isSso;
    this.autonet = isAutonet;
    this.isCloudDms = isCloudDms;

    final Optional<PasswordHash> passwordHashOpt = Optional.ofNullable(user.getLogin())
        .map(Login::getPasswordHash);
    if (passwordHashOpt.isPresent()) {
      this.hashType = passwordHashOpt.map(PasswordHash::getHashType).orElse(null);
      this.passwordSalt = passwordHashOpt.map(PasswordHash::getPasswordSalt).orElse(null);
      this.passwordHash = passwordHashOpt.map(PasswordHash::getPasswordHash).orElse(null);
    }
  }

  private static String[] findUserAuthorities(final EshopUser eshopUser) {
    final List<String> roles =
        eshopUser.getGroupUsers().stream()
            .map(GroupUser::getEshopGroup)
            .flatMap(group -> group.getGroupRoles().stream())
            .map(groupRole -> groupRole.getEshopRole().getName()).collect(Collectors.toList());
    return roles.stream().toArray(String[]::new);
  }

  /**
   * Checks if the login as sale on behalf of a customer.
   *
   * @return <code>true</code> if the login is "on behalf", return <code>false</code> otherwise.
   */
  public boolean isSaleOnBehalf() {
    return !Objects.isNull(this.salesId);
  }

  /**
   * Returns the password and salt as string.
   *
   * @return the encoded password and salt
   */
  public String getEncodedPasswordAndSaltAsString() {
    if (this.hashType == null || HashType.SHA_512 != this.hashType) {
      return this.getPassword();
    }
    return HashUtils.getEncodedPasswordAndSaltAsString(passwordHash, passwordSalt);
  }

  private static String buildUniqueUsername(final Long userId, final Long salesId) {
    if (Objects.isNull(salesId)) {
      return String.valueOf(userId);
    }
    return String.valueOf(userId + SagConstants.UNDERSCORE + salesId);
  }

  public static EshopUserDetails buildEshopUserDetails(EshopUser user, Long salesId,
      String locatedAffiliate, boolean isSso) {
    return new EshopUserDetails(user, salesId, locatedAffiliate, isSso, false, false);
  }

  public static EshopUserDetails buildFinalCustomerUserDetails(EshopUser user,
      String locatedAffiliate, boolean isSso) {
    return new EshopUserDetails(user, null, locatedAffiliate, isSso, false, false);
  }

  public static EshopUserDetails buildAutonetUserDetails(EshopUser user, String locatedAffiliate) {
    return new EshopUserDetails(user, null, locatedAffiliate, false, true, false);
  }

  public static EshopUserDetails buildCloudDmsUserDetails(EshopUser user, String locatedAffiliate) {
    return new EshopUserDetails(user, null, locatedAffiliate, false, false, true);
  }

  public static EshopUserDetails buildVirtualUserDetails(EshopUser user, String locatedAffiliate,
      boolean isCloudDms) {
    return new EshopUserDetails(user, null, locatedAffiliate, false, false, isCloudDms);
  }
}
