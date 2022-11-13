package com.sagag.eshop.service.user;

import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.service.exception.UserValidationException;
import com.sagag.eshop.service.exception.UserValidationException.UserErrorCase;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import javax.transaction.Transactional;

@Component
@Transactional
public class UserSearchFactory {

  @Autowired
  private List<IEshopUserFinder> eshopUserFinders;

  /**
   * Returns the selected user by username case sensitive.
   *
   */
  public Optional<EshopUser> searchUsernameCaseSensitive(final String username,
      final String affiliate) throws UserValidationException {
    return eshopUserFinders.stream().filter(finder -> finder.inputType() == LoginInputType.USERNAME)
        .findFirst().orElseThrow(exceptionSupplier(username)).findBy(username, affiliate);
  }

  public Optional<EshopUser> searchFinalCustomerUsername(final String username,
      final String affiliate) throws UserValidationException {
    return eshopUserFinders.stream()
        .filter(finder -> finder.inputType() == LoginInputType.FINAL_CUSTOMER_USERNAME)
        .findFirst().orElseThrow(exceptionSupplier(username)).findBy(username, affiliate);
  }

  /**
   * Returns the selected user by multiple login input.
   *
   */
  public Optional<EshopUser> searchEshopUserByInput(final String input, final String affiliate)
      throws UserValidationException {
    if (StringUtils.isBlank(affiliate)) {
      final String errorMsg = String.format("Invalid affiliate = %s", affiliate);
      throw new UserValidationException(UserErrorCase.UE_IAF_001, errorMsg);
    }
    if (StringUtils.isBlank(input)) {
      final String errorMsg = String.format("Invalid username = %s", input);
      throw new UserValidationException(UserErrorCase.UE_IUN_001, errorMsg);
    }
    return getEshopUserFinder(input, affiliate)
        .orElseThrow(exceptionSupplier(input))
        .findBy(input, affiliate);
  }

  private Supplier<UnsupportedOperationException> exceptionSupplier(String input) {
    return () -> {
      final String errorMsg = String.format("Unsupport login input type with %s", input);
      return new UnsupportedOperationException(errorMsg);
    };
  }

  private Optional<IEshopUserFinder> getEshopUserFinder(String input, String affiliate) {
    return eshopUserFinders.stream()
        .filter(finder -> finder.isMatchedFinder(input, affiliate)).findFirst();
  }
}
