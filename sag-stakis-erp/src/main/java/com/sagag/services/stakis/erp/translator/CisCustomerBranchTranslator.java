package com.sagag.services.stakis.erp.translator;

import static com.sagag.services.common.utils.XmlUtils.getValueOpt;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.sagag.services.common.profiles.CzProfile;
import com.sagag.services.common.translator.IDataTranslator;
import com.sagag.services.domain.sag.external.CustomerBranch;
import com.sagag.services.stakis.erp.wsdl.cis.ArrayOfSalesOutlet;
import com.sagag.services.stakis.erp.wsdl.cis.SalesOutlet;
import com.sagag.services.stakis.erp.wsdl.cis.SalesOutletList;

@Component
@CzProfile
public class CisCustomerBranchTranslator
  implements IDataTranslator<SalesOutletList, Optional<CustomerBranch>> {

  @Override
  public Optional<CustomerBranch> translateToConnect(SalesOutletList salesOutletList) {
    final String defBranchId = getValueOpt(salesOutletList.getDefault()).orElse(StringUtils.EMPTY);

    final Optional<ArrayOfSalesOutlet> arrayOfSalesOutletOpt =
        getValueOpt(salesOutletList.getSalesOutlets());
    final List<SalesOutlet> salesOutlets = arrayOfSalesOutletOpt
        .map(ArrayOfSalesOutlet::getSalesOutlet).orElse(Collections.emptyList());
    if (CollectionUtils.isEmpty(salesOutlets)) {
      return Optional.empty();
    }

    // Customer Branch association
    final Optional<SalesOutlet> filteredSalesOutletOpt = salesOutlets.stream()
        .filter(saleOutlet -> defBranchId.equals(
            getValueOpt(saleOutlet.getId()).orElse(StringUtils.EMPTY))).findFirst();
    return filteredSalesOutletOpt.map(salesOutlet -> {
      final String branchId = getValueOpt(salesOutlet.getId()).orElse(StringUtils.EMPTY);
      return CustomerBranch.builder().branchId(branchId)
      .branchName(getValueOpt(salesOutlet.getDescription()).orElse(StringUtils.EMPTY)).build();
    });
  }

}
