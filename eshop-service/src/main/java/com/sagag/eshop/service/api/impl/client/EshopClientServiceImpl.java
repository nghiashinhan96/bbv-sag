package com.sagag.eshop.service.api.impl.client;

import com.sagag.eshop.repo.api.EshopRoleRepository;
import com.sagag.eshop.repo.api.client.ClientRoleRepository;
import com.sagag.eshop.repo.api.client.EshopClientRepository;
import com.sagag.eshop.repo.api.client.EshopClientResourceRepository;
import com.sagag.eshop.repo.api.client.VEshopClientRepository;
import com.sagag.eshop.repo.entity.client.ClientRole;
import com.sagag.eshop.repo.entity.client.EshopClient;
import com.sagag.eshop.service.api.EshopClientService;
import com.sagag.eshop.service.converter.client.VEshopClientConverter;
import com.sagag.eshop.service.dto.client.CreatedEshopClientDto;
import com.sagag.eshop.service.dto.client.EshopClientCriteria;
import com.sagag.eshop.service.dto.client.EshopClientDto;
import com.sagag.services.common.security.CompositePasswordEncoder;
import com.sagag.services.domain.eshop.common.EshopAuthority;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

@Service
@Slf4j
public class EshopClientServiceImpl implements EshopClientService {

  private static final boolean DF_ACTIVE_MODE = true;

  @Autowired
  private VEshopClientRepository vEshopClientRepo;

  @Autowired
  private VEshopClientConverter vEshopClientConverter;

  @Autowired
  private ClientRoleRepository clientRoleRepo;

  @Autowired
  private EshopClientRepository eshopClientRepo;

  @Autowired
  private EshopClientResourceRepository eshopClientResourceRepo;

  @Autowired
  private EshopRoleRepository eshopRoleRepo;

  @Autowired
  private CompositePasswordEncoder passwordEncoder;

  @Override
  public Optional<EshopClientDto> findActiveClientByClientName(String clientName) {
    log.debug("Returning eshop client by name = {}", clientName);
    if (StringUtils.isBlank(clientName)) {
      return Optional.empty();
    }
    return vEshopClientRepo.findByClientName(clientName).map(vEshopClientConverter);
  }

  @Override
  @Transactional
  public CreatedEshopClientDto createEshopClient(EshopClientCriteria eshopClientCriteria) {
    log.debug("Creating eshop client info by request = {}", eshopClientCriteria);
    Assert.notNull(eshopClientCriteria, "The given criteria must not be null");
    Assert.hasText(eshopClientCriteria.getClientName(),
        "The given client name must not be blank");
    Assert.hasText(eshopClientCriteria.getResourceName(),
        "The given resource name must not be blank");
    Assert.notEmpty(eshopClientCriteria.getAllowedAuthorities(),
        "The given role list must not be empty");

    final EshopClient eshopClient = new EshopClient();
    eshopClient.setClientName(eshopClientCriteria.getClientName());

    if (eshopClientRepo.exists(Example.of(eshopClient))) {
      throw new IllegalArgumentException("Existing this client name");
    }

    final String clientSecrect = UUID.randomUUID().toString();
    eshopClient.setClientSecret(passwordEncoder.encodeBlockVar(clientSecrect));
    eshopClientResourceRepo.findIdByResourceName(eshopClientCriteria.getResourceName())
    .ifPresent(eshopClient::setResourceId);

    eshopClient.setActive(DF_ACTIVE_MODE);

    final int createdEshopClientId = eshopClientRepo.save(eshopClient).getId();

    createClientRoles(createdEshopClientId, eshopClientCriteria.getAllowedAuthorities());

    final CreatedEshopClientDto createdEshopClient = new CreatedEshopClientDto();
    createdEshopClient.setId(createdEshopClientId);
    createdEshopClient.setClientName(eshopClient.getClientName());
    createdEshopClient.setClientSecret(clientSecrect);

    return createdEshopClient;
  }

  private void createClientRoles(final int createdEshopClientId,
      final List<EshopAuthority> authorities) {
    final List<String> roles = authorities.stream().map(EshopAuthority::name)
        .collect(Collectors.toList());
    final List<ClientRole> clientRoles = eshopRoleRepo.findEshopRolesByName(roles).stream()
        .map(role -> new ClientRole(createdEshopClientId, role.getId()))
        .collect(Collectors.toList());
    clientRoleRepo.saveAll(clientRoles);
  }

  @Override
  public Page<EshopClientDto> findAllActiveEshopClient(Pageable pageable) {
    log.debug("Returning all active eshop client by pageable = {}", pageable);
    Assert.notNull(pageable, "The given pageable must not be null");
    return vEshopClientRepo.findAll(pageable).map(vEshopClientConverter);
  }

}
