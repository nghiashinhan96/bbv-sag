package com.sagag.eshop.service.api.impl.client;

import com.sagag.eshop.repo.api.EshopRoleRepository;
import com.sagag.eshop.repo.api.client.ClientRoleRepository;
import com.sagag.eshop.repo.api.client.EshopClientRepository;
import com.sagag.eshop.repo.api.client.EshopClientResourceRepository;
import com.sagag.eshop.repo.api.client.VEshopClientRepository;
import com.sagag.eshop.repo.entity.client.VEshopClient;
import com.sagag.eshop.service.converter.client.VEshopClientConverter;
import com.sagag.eshop.service.dto.client.EshopClientDto;
import com.sagag.services.common.security.CompositePasswordEncoder;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class EshopClientServiceImplTest {

  @InjectMocks
  private EshopClientServiceImpl service;

  @Mock
  private VEshopClientRepository vEshopClientRepo;

  @Mock
  private ClientRoleRepository clientRoleRepo;

  @Mock
  private EshopClientRepository eshopClientRepo;

  @Mock
  private EshopClientResourceRepository eshopClientResourceRepo;

  @Mock
  private EshopRoleRepository eshopRoleRepo;

  @Mock
  private CompositePasswordEncoder passwordEncoder;

  @Mock
  private VEshopClientConverter vEshopClientConverter;

  @Test
  public void shouldNotFoundEmptyClientName() {
    final String clientName = StringUtils.EMPTY;
    Optional<EshopClientDto> client = service.findActiveClientByClientName(clientName);
    Assert.assertThat(client.isPresent(), Matchers.is(false));
  }

  @Test
  public void shouldNotFoundInvalidClientName() {
    final String clientName = "eshop-client";

    Mockito.when(vEshopClientRepo.findByClientName(Mockito.eq(clientName)))
    .thenReturn(Optional.empty());

    Optional<EshopClientDto> client = service.findActiveClientByClientName(clientName);

    Assert.assertThat(client.isPresent(), Matchers.is(false));

    Mockito.verify(vEshopClientRepo, Mockito.times(1)).findByClientName(Mockito.eq(clientName));
  }

  @Test
  public void shouldFoundClientName() {
    final String clientName = "eshop-web";

    VEshopClient vClient = initEshopClient(clientName);
    Mockito.when(vEshopClientRepo.findByClientName(Mockito.eq(clientName)))
    .thenReturn(Optional.of(vClient));
    Mockito.when(vEshopClientConverter.apply(vClient)).thenReturn(initEshopClientDto(clientName));

    Optional<EshopClientDto> client = service.findActiveClientByClientName(clientName);

    Assert.assertThat(client.isPresent(), Matchers.is(true));

    Mockito.verify(vEshopClientRepo, Mockito.times(1)).findByClientName(Mockito.eq(clientName));
    Mockito.verify(vEshopClientConverter, Mockito.times(1)).apply(Mockito.eq(vClient));
  }

  private VEshopClient initEshopClient(String clientName) {
    VEshopClient client = new VEshopClient();
    client.setClientName(clientName);
    return client;
  }

  private EshopClientDto initEshopClientDto(String clientName) {
    EshopClientDto client = new EshopClientDto();
    client.setClientName(clientName);
    return client;
  }

}
