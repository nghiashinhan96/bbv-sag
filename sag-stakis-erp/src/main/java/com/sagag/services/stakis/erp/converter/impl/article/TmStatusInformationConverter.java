package com.sagag.services.stakis.erp.converter.impl.article;

import static com.sagag.services.common.utils.XmlUtils.getValueOpt;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import com.sagag.services.common.profiles.CzProfile;
import com.sagag.services.domain.sag.erp.ErpArticleMemo;
import com.sagag.services.stakis.erp.wsdl.tmconnect.KeyValueItem;

@Component
@CzProfile
public class TmStatusInformationConverter
  implements Function<List<KeyValueItem>, List<ErpArticleMemo>> {

  @Override
  public List<ErpArticleMemo> apply(List<KeyValueItem> items) {
    if (CollectionUtils.isEmpty(items)) {
      return Collections.emptyList();
    }
    return items.stream().map(articleKeyValueItemConverter()).collect(Collectors.toList());
  }

  private static Function<KeyValueItem, ErpArticleMemo> articleKeyValueItemConverter() {
    return keyValueItem -> {
      final ErpArticleMemo artMemo = new ErpArticleMemo();
      getValueOpt(keyValueItem.getKey()).ifPresent(artMemo::setStatusKey);
      getValueOpt(keyValueItem.getValue()).ifPresent(artMemo::setStatusValue);
      return artMemo;
    };
  }

}
