package com.sagag.services.hazelcast.builder;

import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.elasticsearch.domain.brand_sorting.BrandPriorityDoc;
import com.sagag.services.hazelcast.domain.categories.CachedBrandPriorityDto;

import org.apache.commons.io.IOUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RunWith(SpringRunner.class)
public class BrandPriorityBuilderTest {

  @InjectMocks
  private BrandPriorityBuilder builder;

  @Test
  public void testBuildCachedBrandPriorityObject() throws IOException {
    final String json =
        IOUtils.toString(this.getClass().getResourceAsStream("/json/brand_priority_402.json"), StandardCharsets.UTF_8);
    final BrandPriorityDoc doc = SagJSONUtil.convertJsonToObject(json, BrandPriorityDoc.class);
    CachedBrandPriorityDto priority = builder.apply(doc);
    Assert.assertThat(priority.getGaid(), Matchers.is("402"));
  }

}
