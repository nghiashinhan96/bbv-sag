package com.sagag.services.ivds.utils;

import com.google.common.collect.Lists;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.elasticsearch.domain.formatga.FormatGaDoc;
import com.sagag.services.elasticsearch.domain.formatga.FormatGaElement;
import com.sagag.services.elasticsearch.domain.vehicle.VehicleDoc;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import lombok.experimental.UtilityClass;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Utilities for IvdsDataTest.
 */
@UtilityClass
public final class IvdsDataTestUtils {

  private static final String DATA_DIRECTORY = "src/test/resources/data/";

  protected static FormatGaDoc formatGaDoc(String jsonFilePath) throws IOException {
    FormatGaDoc formatGaDoc = SagJSONUtil.convertJsonToObject(
        readFromTestResources(jsonFilePath), FormatGaDoc.class);
    sortFormatGaElements(formatGaDoc.getElements());
    return formatGaDoc;
  }

  public static ArticleDocDto article(String jsonFilePath) throws IOException {
    return SagJSONUtil.convertJsonToObject(
        readFromTestResources(jsonFilePath), ArticleDocDto.class);
  }

  protected static String readFromTestResources(String filePath) throws IOException {
    return FileUtils.readFileToString(
        new File(FilenameUtils.separatorsToSystem(DATA_DIRECTORY + filePath)), "UTF-8");
  }

  protected static void sortFormatGaElements(List<FormatGaElement> elements) {
    Collections.sort(elements, (ele1, ele2) -> {
      int result = sortFormatGaElementByField(ele1.getSort(), ele2.getSort());
      if (Integer.valueOf(0).equals(result)) {
        return sortFormatGaElementByField(ele1.getOrder(), ele2.getOrder());
      }
      return result;
    });
  }

  protected static int sortFormatGaElementByField(final String field1, final String field2) {
    Integer obj1 = null;
    if (!StringUtils.isEmpty(field1)) {
      obj1 = Integer.valueOf(field1);
    }
    Integer obj2 = null;
    if (!StringUtils.isEmpty(field2)) {
      obj2 = Integer.valueOf(field2);
    }
    if (obj1 == null || obj2 == null) {
      return -1;
    }
    return obj1.compareTo(obj2);
  }

  public static List<VehicleDoc> getMockedVehicleDocList() {
    List<VehicleDoc> docs = Lists.newArrayList();

    VehicleDoc doc = new VehicleDoc();
    doc.setId("V0001");
    doc.setVehId(doc.getId());
    doc.setVehicleName("2.0 i");
    doc.setSort(15);
    docs.add(doc);

    VehicleDoc doc1 = new VehicleDoc();
    doc1.setId("V0002");
    doc1.setVehId(doc1.getId());
    doc1.setVehicleName("1.6 i KAT");
    doc1.setSort(12);
    docs.add(doc1);

    return docs;
  }

}
