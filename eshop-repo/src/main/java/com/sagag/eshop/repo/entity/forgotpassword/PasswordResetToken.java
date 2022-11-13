package com.sagag.eshop.repo.entity.forgotpassword;

import com.sagag.eshop.repo.entity.EshopUser;

import lombok.Data;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Table(name = "PASSWORD_RESET_TOKEN")
@Entity
@Data
public class PasswordResetToken {

  /**
   * 4 hours expired
   */
  private static final int EXPIRATION = 60 * 4;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private String token;

  @OneToOne(targetEntity = EshopUser.class, fetch = FetchType.EAGER)
  @JoinColumn(nullable = false, name = "user_id")
  private EshopUser user;

  private Date expiryDate;

  @Column(name = "HASH_USERNAME_CODE")
  private String hashUsernameCode;

  public PasswordResetToken() {
    super();
  }

  public PasswordResetToken(final String token) {
    super();

    this.token = token;
    this.expiryDate = calculateExpiryDate(EXPIRATION);

  }

  public PasswordResetToken(final String token, final EshopUser user, final String hashUsernameCode) {
    super();

    this.token = token;
    this.user = user;
    this.expiryDate = calculateExpiryDate(EXPIRATION);
    this.hashUsernameCode = hashUsernameCode;
  }

  private Date calculateExpiryDate(final int expiryTimeInMinutes) {
    final Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(new Date().getTime());
    cal.add(Calendar.MINUTE, expiryTimeInMinutes);
    return new Date(cal.getTime().getTime());
  }

  public void updateToken(final String token) {
    this.token = token;
    this.expiryDate = calculateExpiryDate(EXPIRATION);
  }

  public boolean isExpired() {
    final Calendar cal = Calendar.getInstance();
    return getExpiryDate().getTime() - cal.getTime().getTime() <= 0;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((expiryDate == null) ? 0 : expiryDate.hashCode());
    result = prime * result + ((token == null) ? 0 : token.hashCode());
    result = prime * result + ((user == null) ? 0 : user.hashCode());
    return result;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final PasswordResetToken other = (PasswordResetToken) obj;
    if (expiryDate == null) {
      if (other.expiryDate != null) {
        return false;
      }
    } else if (!expiryDate.equals(other.expiryDate)) {
      return false;
    }
    if (token == null) {
      if (other.token != null) {
        return false;
      }
    } else if (!token.equals(other.token)) {
      return false;
    }
    if (user == null) {
      if (other.user != null) {
        return false;
      }
    } else if (!user.equals(other.user)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    final StringBuilder builder = new StringBuilder();
    builder.append("Token [String=").append(token).append("]").append("[Expires")
        .append(expiryDate).append("]");
    return builder.toString();
  }

}
