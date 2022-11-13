package com.sagag.services.ax.availability.externalvendor;

import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.externalvendor.ExternalVendorDto;

@RunWith(MockitoJUnitRunner.class)
public class PotentialExternalArticlesFilterTest {

  @InjectMocks
  private PotentialExternalArticlesFilter potentialExternalArticlesFilter;

  @Test
  public void filterArticlesCouldBeSuppliedByExternalVendor_givenArticleHasBrandIdAndGroup()
      throws Exception {
    ArticleDocDto article = new ArticleDocDto();
    article.setIdSagsys("1001074327");
    article.setIdDlnr("4532");
    article.setSagProductGroup("RE");

    List<ArticleDocDto> results =
        potentialExternalArticlesFilter.filterArticlesCouldBeSuppliedByExternalVendor(
            Arrays.asList(article), buildExternalVendors());
    assertThat(results.size(), Matchers.is(1));
  }

  @Test
  public void filterArticlesCouldBeSuppliedByExternalVendor_givenArticleHasNotFoundBrandIdAndGroup()
      throws Exception {
    ArticleDocDto article = new ArticleDocDto();
    article.setIdSagsys("1001074327");
    article.setIdDlnr("1234");
    article.setSagProductGroup("RE");

    List<ArticleDocDto> results =
        potentialExternalArticlesFilter.filterArticlesCouldBeSuppliedByExternalVendor(
            Arrays.asList(article), buildExternalVendors());
    assertThat(results.size(), Matchers.is(0));
  }

  @Test
  public void filterArticlesCouldBeSuppliedByExternalVendor_givenArticleHasOnlyGroup()
      throws Exception {
    ArticleDocDto article = new ArticleDocDto();
    article.setIdSagsys("1001074327");
    article.setSagProductGroup("RE");

    List<ArticleDocDto> results =
        potentialExternalArticlesFilter.filterArticlesCouldBeSuppliedByExternalVendor(
            Arrays.asList(article), buildExternalVendors());
    assertThat(results.size(), Matchers.is(0));
  }

  @Test
  public void filterArticlesCouldBeSuppliedByExternalVendor_givenArticleHasNoBrandIdAndGroup()
      throws Exception {
    ArticleDocDto article = new ArticleDocDto();
    article.setIdSagsys("1001074327");

    List<ArticleDocDto> results =
        potentialExternalArticlesFilter.filterArticlesCouldBeSuppliedByExternalVendor(
            Arrays.asList(article), buildExternalVendors());
    assertThat(results.size(), Matchers.is(1));
  }

  private List<ExternalVendorDto> buildExternalVendors() {
    List<ExternalVendorDto> vendors = new ArrayList<>();

    ExternalVendorDto vendor1 =
        ExternalVendorDto.builder().brandId(4532l).sagArticleGroup("RE").vendorId("859067").build();
    ExternalVendorDto vendor2 = ExternalVendorDto.builder().sagArticleGroup("RE").build();
    ExternalVendorDto vendor3 = ExternalVendorDto.builder().vendorId("859067").build();

    vendors.add(vendor3);
    vendors.add(vendor2);
    vendors.add(vendor1);
    return vendors;
  }
}
