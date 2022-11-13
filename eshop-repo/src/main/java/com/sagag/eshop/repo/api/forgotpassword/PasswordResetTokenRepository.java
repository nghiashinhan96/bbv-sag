package com.sagag.eshop.repo.api.forgotpassword;

import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.repo.entity.forgotpassword.PasswordResetToken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.stream.Stream;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

  PasswordResetToken findByToken(String token);

  /**
   * Returns the password reset token to clean expired token.
   *
   * @param user the selected eshop user
   * @return the result of {@link PasswordResetToken}
   */
  PasswordResetToken findByUser(EshopUser user);

  Stream<PasswordResetToken> findAllByExpiryDateLessThan(Date now);

  void deleteByExpiryDateLessThan(Date now);

  @Modifying
  @Query("delete from PasswordResetToken t where t.expiryDate <= ?1")
  void deleteAllExpiredSince(Date now);

  @Query("SELECT t FROM PasswordResetToken t where t.token = :token and t.hashUsernameCode = :hash")
  PasswordResetToken findByTokenAndHashUsernameCode(@Param("token") String token,
      @Param("hash") String hash);
}
