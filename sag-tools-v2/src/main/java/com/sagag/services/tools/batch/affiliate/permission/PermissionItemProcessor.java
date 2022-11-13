package com.sagag.services.tools.batch.affiliate.permission;

import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.target.EshopGroup;
import com.sagag.services.tools.domain.target.EshopPermission;
import com.sagag.services.tools.domain.target.GroupPermission;
import com.sagag.services.tools.repository.target.TargetEshopPermissionRepository;
import com.sagag.services.tools.repository.target.TargetGroupPermissionRepository;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
@StepScope
@OracleProfile
public class PermissionItemProcessor implements ItemProcessor<EshopGroup, GroupPermission>, InitializingBean {

  @Value("#{'${sag.batch.permission.permission_name:}'}")
  private String permissionName;

  @Value("#{'${sag.batch.permission.enable:}'}")
  private Boolean enable;

  private EshopPermission permission;

  @Autowired
  private TargetEshopPermissionRepository targetEshopPermissionRepository;

  @Autowired
  private TargetGroupPermissionRepository targetGroupPermissionRepository;

  @Override
  public void afterPropertiesSet() throws Exception {
    permission =
        targetEshopPermissionRepository.findOneByPermission(permissionName).orElseThrow(() -> new IllegalArgumentException("Permission does not exist"));
    if (Objects.isNull(enable)) {
      enable = Boolean.TRUE;
    }
  }

  @Override
  public GroupPermission process(EshopGroup group) throws Exception {
    Optional<GroupPermission> existedGroupPermOpt = targetGroupPermissionRepository.findByGroupIdAndPermissionId(group.getId(), permission.getId());
    if (existedGroupPermOpt.isPresent()) {
      GroupPermission existedGroupPerm = existedGroupPermOpt.get();
      if (enable.equals(existedGroupPerm.isAllowed())) {
        return null;
      }
      existedGroupPerm.setAllowed(enable);
      return existedGroupPerm;
    }
    return GroupPermission.builder().eshopGroup(group).eshopPermission(permission).allowed(enable).build();
  }
}
