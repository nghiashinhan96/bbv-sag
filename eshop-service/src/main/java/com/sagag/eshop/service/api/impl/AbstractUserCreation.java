package com.sagag.eshop.service.api.impl;

import com.sagag.eshop.repo.api.EshopUserRepository;
import com.sagag.eshop.repo.api.LanguageRepository;
import com.sagag.eshop.repo.api.LoginRepository;
import com.sagag.eshop.repo.api.SalutationRepository;
import com.sagag.eshop.repo.api.UserSettingsRepository;
import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.repo.entity.Language;
import com.sagag.eshop.repo.entity.Login;
import com.sagag.eshop.repo.entity.Salutation;
import com.sagag.eshop.service.api.LanguageService;
import com.sagag.eshop.service.api.RoleService;
import com.sagag.eshop.service.api.SalutationService;
import com.sagag.eshop.service.helper.CustomerSettingHelper;
import com.sagag.services.common.locale.LocaleContextHelper;
import com.sagag.services.domain.eshop.dto.LanguageDto;
import com.sagag.services.domain.eshop.dto.SalutationDto;
import com.sagag.services.domain.eshop.dto.UserProfileDto;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AbstractUserCreation {

  @Autowired
  private EshopUserRepository userRepository;

  @Autowired
  private LoginRepository loginRepository;

  @Autowired
  private SalutationRepository salutationRepo;

  @Autowired
  protected LanguageRepository languageRepo;

  @Autowired
  protected RoleService roleService;

  @Autowired
  protected SalutationService salutationService;

  @Autowired
  protected UserSettingsRepository userSettingsRepo;

  @Autowired
  protected CustomerSettingHelper customerSettingHelper;

  @Autowired
  protected LanguageService languageService;

  @Autowired
  protected LocaleContextHelper localeContextHelper;

  protected Function<EshopUser, UserProfileDto> userProfileInfoConverter() {
    return eshopUser -> {
      final UserProfileDto userProfileDto = new UserProfileDto();
      userProfileDto.setId(eshopUser.getId());
      userProfileDto.setUserName(eshopUser.getUsername());
      userProfileDto.setSurName(eshopUser.getLastName());
      userProfileDto.setFirstName(eshopUser.getFirstName());
      userProfileDto.setHourlyRate(eshopUser.getHourlyRate());
      userProfileDto.setEmail(eshopUser.getEmail());
      userProfileDto.setPhoneNumber(eshopUser.getPhone());
      userProfileDto.setEmailConfirmation(eshopUser.isEmailConfirmation());
      return userProfileDto;
    };
  }

  protected Function<UserProfileDto, UserProfileDto> languageInfoConverter(EshopUser eshopUser) {
    return profile -> {
      // Language
      profile.setLanguages(getSortedLanguage());
      final Language language = eshopUser.getLanguage();
      final LanguageDto languageDto = new LanguageDto();
      languageDto.setDescription(language.getDescription());
      languageDto.setId(language.getId());
      languageDto.setLangcode(language.getLangcode());
      languageDto.setLangiso(language.getLangiso());
      profile.setLanguageId(language.getId());
      return profile;
    };
  }

  protected List<LanguageDto> getSortedLanguage() {
    return languageService.getAllLanguage().stream()
        .sorted(sortedByDefaultLangByCountry(localeContextHelper.defaultAppLocaleLanguage()))
        .collect(Collectors.toList());
  }

  private static Comparator<LanguageDto> sortedByDefaultLangByCountry(String defLangByCountry) {
    return (lang1, lang2) -> {
      if (StringUtils.equalsIgnoreCase(defLangByCountry, lang1.getLangiso())) {
        return -1;
      }
      if (StringUtils.equalsIgnoreCase(defLangByCountry, lang2.getLangiso())) {
        return 0;
      }
      return NumberUtils.compare(lang1.getId(), lang2.getId());
    };
  }

  protected Function<UserProfileDto, UserProfileDto> salutationInfoConverter(EshopUser eshopUser) {
    return profile -> {
      // Salutation
      profile.setSalutations(salutationService.getProfileSalutations());
      final Salutation salutation = eshopUser.getSalutation();
      final SalutationDto salutationDto = new SalutationDto();
      salutationDto.setId(salutation.getId());
      salutationDto.setDescription(salutation.getDescription());
      salutationDto.setCode(salutation.getCode());
      profile.setSalutationId(salutation.getId());
      return profile;
    };
  }

  protected Function<UserProfileDto, UserProfileDto> roleTypesInfoConverter(EshopUser eshopUser,
      boolean isOtherProfile) {
    return profile -> {
      final String mainUserRole = eshopUser.getRoles().get(0);
      profile.setTypes(roleService.getEshopRolesForUserProfile(mainUserRole, isOtherProfile));
      // Set own role
      roleService.findRoleByName(mainUserRole).ifPresent(
          userRole -> profile.setTypeId(userRole.getId()));
      return profile;
    };
  }

  protected Function<UserProfileDto, UserProfileDto> additionalInfoConverter() {
    return profile -> {
      profile.setVinCall(StringUtils.EMPTY);
      profile.setLicense(StringUtils.EMPTY);
      return profile;
    };
  }

  protected EshopUser createDefaultUser(UserProfileDto userProfileDto, int settingsId) {
    final EshopUser eshopUser = new EshopUser();
    eshopUser.setUsername(userProfileDto.getUserName());
    eshopUser.setEmail(userProfileDto.getEmail());
    eshopUser.setFirstName(userProfileDto.getFirstName());
    eshopUser.setLastName(userProfileDto.getSurName());
    eshopUser.setPhone(userProfileDto.getPhoneNumber());
    eshopUser.setType(userProfileDto.getUserType());
    eshopUser.setSetting(settingsId);
    eshopUser.setLanguage(languageRepo.findById(userProfileDto.getLanguageId()).orElse(null));
    eshopUser.setSalutation(salutationRepo.findById(userProfileDto.getSalutationId()).orElse(null));
    eshopUser.setHourlyRate(userProfileDto.getHourlyRate());
    return userRepository.save(eshopUser);
  }

  protected Login createLogin(EshopUser user) {
    final Login login = new Login();
    login.setUserActive(true);
    login.setEshopUser(user);
    return loginRepository.save(login);
  }
}
