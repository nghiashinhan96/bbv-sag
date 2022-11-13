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
import com.sagag.services.stakis.erp.wsdl.tmconnect.Memo;

@Component
@CzProfile
public class TmMemoTextConverter implements Function<List<Memo>, List<ErpArticleMemo>> {

  @Override
  public List<ErpArticleMemo> apply(List<Memo> memoList) {
    if (CollectionUtils.isEmpty(memoList)) {
      return Collections.emptyList();
    }
    return memoList.stream().map(articleMemoConverter()).collect(Collectors.toList());
  }

  private static Function<Memo, ErpArticleMemo> articleMemoConverter() {
    return m -> {
      final ErpArticleMemo aMemo = new ErpArticleMemo();
      getValueOpt(m.getText()).ifPresent(aMemo::setText);
      getValueOpt(m.getLabel()).ifPresent(aMemo::setLabel);
      getValueOpt(m.getLinkUrl()).ifPresent(aMemo::setLink);
      aMemo.setType(m.getType());
      return aMemo;
    };
  }

}
