package com.sagag.services.tools.batch.user.category_view_setting;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.target.EshopUser;
import com.sagag.services.tools.domain.target.UserSettings;
import com.sagag.services.tools.repository.target.EshopUserRepository;

@Component
@StepScope
@OracleProfile
public class ConvertingCateViewProcessor implements ItemProcessor<UserSettings, UserSettings> {

  @Autowired
  private EshopUserRepository eshopUserRepository;

  @Override
  public UserSettings process(final UserSettings source) {
    Optional<EshopUser> eshopUserOp = eshopUserRepository.findBySettingId(source.getId());

    if (!eshopUserOp.isPresent()) {
      source.setClassicCategoryView(true);
      return source;
    }
    final Date signInDate = eshopUserOp.get().getSignInDate();
    // Not log in or login older one month to Connect, set default to classic
    source.setClassicCategoryView(Objects.isNull(signInDate) || isOlderThanOneMonth(signInDate));
    return source;
  }

  private boolean isOlderThanOneMonth(final Date date) {
    final LocalDate monthAgo = LocalDate.now().minusMonths(1);
    return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().isBefore(monthAgo);
  }
}
