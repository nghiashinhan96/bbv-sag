package com.sagag.services.stakis.erp.converter.impl.article;

import com.sagag.services.common.profiles.CzProfile;
import static com.sagag.services.common.utils.XmlUtils.getValueOpt;
import com.sagag.services.stakis.erp.wsdl.tmconnect.ArrayOfQuantity;
import com.sagag.services.stakis.erp.wsdl.tmconnect.ArrayOfWarehouse;
import com.sagag.services.stakis.erp.wsdl.tmconnect.ErpInformation;
import com.sagag.services.stakis.erp.wsdl.tmconnect.Quantity;
import com.sagag.services.stakis.erp.wsdl.tmconnect.Warehouse;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import org.springframework.stereotype.Component;

@Component
@CzProfile
public class TmQuantityMultipleConverter implements Function<ErpInformation, Optional<Integer>> {

  private static Function<Warehouse, List<Quantity>> quantityConverter() {
    return erpInfo -> getValueOpt(erpInfo.getQuantities()).map(ArrayOfQuantity::getQuantity)
      .orElse(Collections.emptyList());
  }

  private static Function<ErpInformation, List<Warehouse>> warehouseConverter() {
    return erpInfo -> getValueOpt(erpInfo.getWarehouses()).map(ArrayOfWarehouse::getWarehouse)
      .orElse(Collections.emptyList());
  }

  @Override
  public Optional<Integer> apply(ErpInformation erpInfo) {
    if (Objects.isNull(erpInfo)) {
      return Optional.empty();
    }

    return warehouseConverter().apply(erpInfo).stream().findFirst()
      .map(quantityConverter()).orElse(Collections.emptyList()).stream().findFirst()
      .map(Quantity::getQuantityPackingUnit).map(BigDecimal::intValue);
  }

}
