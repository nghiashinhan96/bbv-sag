package com.sagag.services.gtmotive.builder.mainmoduleservice;

import com.sagag.services.gtmotive.dto.response.mainmoduleservice.GtmotivePartsThreeReference;
import com.sagag.services.gtmotive.lang.GtmotiveLanguageProvider;
import com.sagag.services.gtmotive.profile.GtmotiveProfileLoader;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

public abstract class AbstractGtmotivePartsThreeReferencesHandler {

  private static final String TAG_NAME_REFERENCE_DESCRIPTION = "referenceDescription";
  private static final String TAG_NAME_REFERENCE_CODE = "referenceCode";
  private static final String TAG_NAME_AUXILIARI_INFORMATION = "auxiliarInformation";
  private static final String TAG_NAME_SUPPLY_TYPE = "supplyType";

  @Autowired
  private GtmotiveLanguageProvider gtmotiveLanguageProvider;

  @Autowired
  private List<GtmotiveProfileLoader> profileLoaders;

  protected abstract List<GtmotivePartsThreeReference> achieveReferences(Document doc, XPath xpath)
      throws XPathExpressionException;

  protected List<GtmotivePartsThreeReference> achieveReferences(String ref, Document doc,
      XPath xpath) throws XPathExpressionException {
    final XPathExpression multiReferenceExpr = xpath.compile(ref);
    NodeList multiReferences = (NodeList) multiReferenceExpr.evaluate(doc, XPathConstants.NODESET);
    List<GtmotivePartsThreeReference> references = new ArrayList<>();
    for (int i = 0; i <= multiReferences.getLength() - 1; i++) {
      fromElementToReference((Element) multiReferences.item(i)).ifPresent(references::add);
    }
    return references;
  }

  protected Optional<GtmotivePartsThreeReference> fromElementToReference(Element ele) {
    GtmotivePartsThreeReference reference = new GtmotivePartsThreeReference();
    Node referenceCode = ele.getElementsByTagName(TAG_NAME_REFERENCE_CODE).item(0);
    if (Objects.isNull(referenceCode) || StringUtils.isEmpty(referenceCode.getTextContent())) {
      return Optional.empty();
    }
    reference.setCode(referenceCode.getTextContent());
    Node referenceDesc = ele.getElementsByTagName(TAG_NAME_REFERENCE_DESCRIPTION).item(0);
    if (!Objects.isNull(referenceDesc) && !StringUtils.isEmpty(referenceDesc.getTextContent())) {
      reference.setDescription(referenceDesc.getTextContent());
    }

    Node auxiliarInformationNode = ele.getElementsByTagName(TAG_NAME_AUXILIARI_INFORMATION).item(0);
    if (!Objects.isNull(auxiliarInformationNode)
        && !StringUtils.isEmpty(auxiliarInformationNode.getTextContent())) {
      reference.setAuxiliarInformation((auxiliarInformationNode.getTextContent()));
    }

    Node supplyTypeNode = ele.getElementsByTagName(TAG_NAME_SUPPLY_TYPE).item(0);
    if (!Objects.isNull(supplyTypeNode) && !StringUtils.isEmpty(supplyTypeNode.getTextContent())) {
      reference.setSupplyType((supplyTypeNode.getTextContent()));
    }
    return Optional.of(reference);
  }

  protected String getSupplyTypeNew() {
    Locale locale = gtmotiveLanguageProvider.getUserLang();
    return profileLoaders.stream()
        .filter(loader -> loader.locale().equals(locale))
        .map(GtmotiveProfileLoader::getSupplyTypeNew).findFirst()
        .orElseGet(() -> StringUtils.EMPTY);
  }

  protected String getSupplyTypeStandard() {
    Locale locale = gtmotiveLanguageProvider.getUserLang();
    return profileLoaders.stream()
        .filter(loader -> loader.locale().equals(locale))
        .map(GtmotiveProfileLoader::getSupplyTypeStandard).findFirst()
        .orElseGet(() -> StringUtils.EMPTY);
  }

  protected String getSupplyTypeEconomic() {
    Locale locale = gtmotiveLanguageProvider.getUserLang();
    return profileLoaders.stream()
        .filter(loader -> loader.locale().equals(locale))
        .map(GtmotiveProfileLoader::getSupplyTypeEconomic).findFirst()
        .orElseGet(() -> StringUtils.EMPTY);
  }
}
