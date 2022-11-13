package com.sagag.services.elasticsearch.utils;

import com.sagag.services.elasticsearch.dto.ArticleFilteringResponse;
import java.util.List;
import java.util.Objects;
import lombok.experimental.UtilityClass;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;

@UtilityClass
public final class AssertESUtils {

  public static void assertExistAndEqualsOnlyOneValue(List<Object> aggregation, Object value) {
    if (CollectionUtils.isEmpty(aggregation) || Objects.isNull(value)
        || CollectionUtils.size(aggregation) != NumberUtils.INTEGER_ONE.intValue()) {
      throw new AssertionError("Assertion error");
    }
    Object actualObj = aggregation.get(0);
    if (actualObj instanceof Double) {
      boolean comparedValue =
          NumberUtils.toDouble(actualObj.toString()) == NumberUtils.toDouble(value.toString());
      Assert.assertThat(comparedValue, Matchers.equalTo(true));
    } else if (actualObj instanceof String) {
      Assert.assertThat(StringUtils.equals(value.toString(), actualObj.toString()),
          Matchers.equalTo(true));
    } else {
      throw new UnsupportedOperationException("No support that assertion");
    }
  }

  public static long getTotalElements(ArticleFilteringResponse response) {
    if (response == null || response.getArticles() == null) {
      return 0;
    }
    return response.getArticles().getTotalElements();
  }

}
