package com.sagag.services.tools.batch.sales.registration;

import com.sagag.services.tools.domain.target.EshopGroup;
import com.sagag.services.tools.domain.target.EshopUser;
import com.sagag.services.tools.domain.target.GroupUser;
import com.sagag.services.tools.domain.target.Login;
import com.sagag.services.tools.domain.target.PasswordHash;
import com.sagag.services.tools.domain.target.UserSettings;
import com.sagag.services.tools.domain.target.UserSettings.UserSettingsBuilder;
import com.sagag.services.tools.repository.target.CollectiveDeliveryRepository;
import com.sagag.services.tools.repository.target.DeliveryTypesRepository;
import com.sagag.services.tools.repository.target.EshopGroupRepository;
import com.sagag.services.tools.repository.target.EshopUserRepository;
import com.sagag.services.tools.repository.target.GroupUserRepository;
import com.sagag.services.tools.repository.target.InvoiceTypeRepository;
import com.sagag.services.tools.repository.target.LoginRepository;
import com.sagag.services.tools.repository.target.PaymentMethodRepository;
import com.sagag.services.tools.repository.target.UserSettingsRepository;
import com.sagag.services.tools.support.EshopAuthority;
import com.sagag.services.tools.support.HashType;
import com.sagag.services.tools.utils.UserSettingConstants;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@StepScope
@Component
public class CreateSalesUserItemWriter implements ItemWriter<EshopUser> {

  private static final String DEFAULT_PASS_HASH = "Fdl8lcev2gXKE";

  @Autowired
  private EshopUserRepository eshopUserRepo;

  @Autowired
  private CollectiveDeliveryRepository collectiveDeliveryRepository;

  @Autowired
  private DeliveryTypesRepository deliveryTypesRepository;

  @Autowired
  private PaymentMethodRepository paymentMethodRepository;

  @Autowired
  private InvoiceTypeRepository invoiceTypeRepository;

  @Autowired
  private UserSettingsRepository userSettingsRepository;

  @Autowired
  private LoginRepository loginRepository;

  @Autowired
  private EshopGroupRepository eshopGroupRepo;

  @Autowired
  private GroupUserRepository groupUserRepo;

  @Override
  @Transactional
  public void write(List<? extends EshopUser> sales) throws Exception {
    if (CollectionUtils.isEmpty(sales)) {
      return;
    }

    // Create User
    UserSettingsBuilder settingBuilder = createDefaultUserSettingsBuilder();
    sales.forEach(eUser -> eUser.setSetting(userSettingsRepository.save(settingBuilder.build()).getId()));
    eshopUserRepo.saveAll(sales);

    // Create Login for user
    final EshopGroup eshopGroup = findEshopGroupForSaleAssistant().orElseThrow(() -> new Exception("eshop group not found"));
    sales.forEach(eUser -> {
      createLogin(eUser);
      createGroupUser(eUser, eshopGroup);
    });
  }

  private void createGroupUser(EshopUser eshopUser, EshopGroup eshopGroup) {
    final GroupUser groupUser = GroupUser.builder()
        .eshopGroup(eshopGroup)
        .eshopUser(eshopUser)
        .build();
    groupUserRepo.save(groupUser);
  }

  private Optional<EshopGroup> findEshopGroupForSaleAssistant() {
    return eshopGroupRepo.findByName(EshopAuthority.SALES_ASSISTANT.name());
  }

  private void createLogin(EshopUser newEshopUser) {
    Login login = Login.builder()
        .passwordHash(new PasswordHash(DEFAULT_PASS_HASH, HashType.BLCK_VAR))
        .isUserActive(true)
        .eshopUser(newEshopUser)
        .build();
    loginRepository.save(login);
  }

  private UserSettingsBuilder createDefaultUserSettingsBuilder() {
    return UserSettings.builder()
        .allocationId(UserSettingConstants.ALLOCATION_ID_DEFAULT)
        .collectiveDelivery(collectiveDeliveryRepository.findById(UserSettingConstants.COLLECTIVE_DELIVERY_ID_DEFAULT).orElse(null))
        .deliveryType(deliveryTypesRepository.findById(UserSettingConstants.DELIVERY_ID_DEFAULT).orElse(null))
        .paymentMethod(paymentMethodRepository.findById(UserSettingConstants.PAYMENT_METHOD_ID_DEFAULT).orElse(null))
        .invoiceType(invoiceTypeRepository.findById(UserSettingConstants.INVOICE_TYPE_ID_DEFAULT).orElse(null))
        .classicCategoryView(UserSettingConstants.CLASSIC_CATEGORY_VIEW_DEFAULT);
  }
}
