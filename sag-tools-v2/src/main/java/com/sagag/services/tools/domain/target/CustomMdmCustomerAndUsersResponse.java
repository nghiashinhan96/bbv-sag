package com.sagag.services.tools.domain.target;

import com.sagag.services.tools.domain.mdm.DvseCustomerUser;
import com.sagag.services.tools.domain.mdm.DvseCustomerUserInfo;
import com.sagag.services.tools.domain.mdm.DvseMainModule;
import com.sagag.services.tools.utils.UserModuleBuilder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Predicate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomMdmCustomerAndUsersResponse implements Serializable {

  private static final long serialVersionUID = 7186230659191363137L;

  private ExtMdmCustomer customer;
  
  private Map<Long, ExtMdmUser> users;
  
  private Long extOrgId;
  
  private List<ExternalUser> extUsers;
  
  private Map<Long , DvseCustomerUser> dvseCustUsers;
  
  private List<String> notWritenCustomerNumberList;
  
  public void addCustomer(String custId, String custName) {
    Assert.hasText(custId, "The given custId must not be empty");
    Assert.hasText(custName, "The given custName must not be empty");
    setCustomer(new ExtMdmCustomer(custId, custName));
  }
  public void addUser(final DvseCustomerUserInfo info) {
    Assert.notNull(info, "The given createdUser must not be empty");
    addUser(info.getUsername(), info.getPassword());
  }
  
  private void addUser(final String username, final String password) {
    Assert.hasText(username, "The given username must not be empty");
    Assert.hasText(password, "The given password must not be empty");
    if (users == null) {
      users = new HashMap<>();
    }
    final Long userId = findUserId(username, password);
    Assert.notNull(userId, "The given userId must not be empty");
    users.putIfAbsent(userId, new ExtMdmUser(username, password));
  }
  
  private Long findUserId(final String username, final String password) {
    for (Entry<Long, DvseCustomerUser> entry : dvseCustUsers.entrySet()) {
      if (userFilter(username, password).test(entry.getValue())) {
        return entry.getKey();
      }
    }
    return null;
  }
  
  private static Predicate<DvseCustomerUser> userFilter(final String username, final String password) {
    return item -> {
      final Optional<DvseMainModule> moduleOpt = UserModuleBuilder.filterUserInfoByList(item.getModules());
      if (!moduleOpt.isPresent()) {
        return false;
      }
      DvseMainModule module = moduleOpt.get();
      final String createdUsername = UserModuleBuilder.getUsernameByKey(module.getKeyValues()).orElse(StringUtils.EMPTY);
      final String createdPass = UserModuleBuilder.getPasswordByKey(module.getKeyValues()).orElse(StringUtils.EMPTY);
      return StringUtils.equalsIgnoreCase(createdUsername, username) && StringUtils.equalsIgnoreCase(createdPass, password);
    };
  }
  
  public String getCustId() {
    return getCustomer().getCustId();
  }
  
  public String getCustName() {
    return getCustomer().getCustName();
  }
  
  public ExtMdmUser getUserById(Long id) {
    return users.get(id);
  }
  
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public class ExtMdmCustomer {
    private String custId;
    private String custName;
  }
  
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public class ExtMdmUser {
    private String username;
    private String password;
  }

}
