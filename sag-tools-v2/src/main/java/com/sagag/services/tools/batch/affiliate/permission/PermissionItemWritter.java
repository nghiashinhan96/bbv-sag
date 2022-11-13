package com.sagag.services.tools.batch.affiliate.permission;

import com.sagag.services.tools.config.OracleProfile;
import com.sagag.services.tools.domain.target.GroupPermission;
import com.sagag.services.tools.repository.target.TargetGroupPermissionRepository;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@StepScope
@OracleProfile
public class PermissionItemWritter implements ItemWriter<GroupPermission> {

  @Autowired
  private TargetGroupPermissionRepository targetGroupPermissionRepository;

  @Override
  public void write(List<? extends GroupPermission> items) throws Exception {
    List<GroupPermission> groupPermissions = items.stream().filter(Objects::nonNull).collect(Collectors.toList());
    if (CollectionUtils.isEmpty(groupPermissions)) {
      return;
    }
    targetGroupPermissionRepository.saveAll(groupPermissions);
  }
}
