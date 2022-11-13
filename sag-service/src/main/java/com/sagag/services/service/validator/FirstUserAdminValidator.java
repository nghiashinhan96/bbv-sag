package com.sagag.services.service.validator;

import com.sagag.eshop.repo.api.EshopUserRepository;
import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.service.exception.UserValidationException;
import com.sagag.eshop.service.exception.UserValidationException.UserErrorCase;
import com.sagag.services.common.enums.UserType;
import com.sagag.services.domain.eshop.dto.UserRegistrationDto;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class FirstUserAdminValidator extends AbstractUserAdminValidator {

  @Autowired
  private EshopUserRepository eshopUserRepo;

  @Override
  public boolean validate(UserRegistrationDto criteria) throws UserValidationException {
    super.validate(criteria);
    final String custNumber = criteria.getCustomerNumber();
    List<EshopUser> listOfExistedUsers = eshopUserRepo.findUsersByCustomerNumber(custNumber);
    Optional<EshopUser> existedOnBehalfAdminOptional = listOfExistedUsers.stream()
        .filter(e -> !StringUtils.equalsIgnoreCase(e.getType(), UserType.ON_BEHALF_ADMIN.name()))
        .findFirst();
    if (existedOnBehalfAdminOptional.isPresent()) {
      throw new UserValidationException(UserErrorCase.UE_ICU_001,
          "Admin User name is existing in same customer.");
    }
    return true;
  }

}
