package com.sagag.services.ivds.utils;

import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.domain.article.ArticlePartDto;

import lombok.extern.slf4j.Slf4j;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * UT for {@link com.sagag.services.ivds.utils.OEAndIamNumberBuilder}.
 *
 */
@RunWith(SpringRunner.class)
@Slf4j
public class OEAndIamNumberBuilderTest {

  @InjectMocks
  private OEAndIamNumberBuilder builder;

  @Test
  public void shouldEmptyMapWithEmptyArticleParts() {

    // makes
    final Map<String, String> makes = new HashMap<>();
    makes.put("1", "AUDI");
    makes.put("2", "TOYOTA");
    makes.put("3", "FORD");
    makes.put("4", "BMW");

    final Map<String, List<String>> result = builder.build(Collections.emptyList(), makes);

    Assert.assertThat(result.isEmpty(), Matchers.is(true));
  }

  @Test
  public void shouldEmptyMapWithEmptyMakes() {
    // article parts
    final List<ArticlePartDto> parts = new ArrayList<>();
    parts.add(ArticlePartDto.builder().brandid("1").pnrn("AAAA").build());
    parts.add(ArticlePartDto.builder().brandid("1").pnrn("EEEE").build());
    parts.add(ArticlePartDto.builder().brandid("2").pnrn("BBBB").build());
    parts.add(ArticlePartDto.builder().brandid("3").pnrn("CCCC").build());
    parts.add(ArticlePartDto.builder().brandid("4").pnrn("DDDD").build());

    final Map<String, List<String>> result = builder.build(parts, Collections.emptyMap());

    Assert.assertThat(result.isEmpty(), Matchers.is(true));
  }

  @Test
  public void shouldGroupingOENumbersByBrands() {
    // article parts
    final List<ArticlePartDto> parts = new ArrayList<>();
    parts.add(ArticlePartDto.builder().brandid("1").pnrn("AAAA").build());
    parts.add(ArticlePartDto.builder().brandid("1").pnrn("EEEE").build());
    parts.add(ArticlePartDto.builder().brandid("2").pnrn("BBBB").build());
    parts.add(ArticlePartDto.builder().brandid("3").pnrn("CCCC").build());
    parts.add(ArticlePartDto.builder().brandid("4").pnrn("DDDD").build());
    parts.add(ArticlePartDto.builder().brandid("9").pnrn("QQQQ").build());

    // makes
    final Map<String, String> makes = new HashMap<>();
    makes.put("1", "AUDI");
    makes.put("2", "TOYOTA");
    makes.put("3", "FORD");
    makes.put("4", "BMW");

    final Map<String, List<String>> result = builder.build(parts, makes);

    log.debug("Result = {}", SagJSONUtil.convertObjectToPrettyJson(result));

    Assert.assertThat(result.isEmpty(), Matchers.is(false));
    Assert.assertThat(makes.size(), Matchers.is(result.size()));
  }
}
