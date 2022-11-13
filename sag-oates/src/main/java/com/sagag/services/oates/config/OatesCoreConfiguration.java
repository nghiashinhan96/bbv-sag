package com.sagag.services.oates.config;

import java.util.Optional;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sagag.services.oates.api.OatesService;
import com.sagag.services.oates.dto.OatesEquipmentProductsDto;
import com.sagag.services.oates.dto.OatesVehicleDto;

@Configuration
public class OatesCoreConfiguration {

  @Bean
  @ConditionalOnMissingBean(OatesService.class)
  public OatesService defaultOatesService() {
    return new OatesService() {

      @Override
      public Optional<OatesVehicleDto> searchOatesVehicle(String vehicleId) {
        return Optional.empty();
      }

      @Override
      public OatesEquipmentProductsDto searchOatesEquipment(String href) {
        return new OatesEquipmentProductsDto();
      }

    };
  }
}
