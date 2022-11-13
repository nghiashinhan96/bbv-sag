package com.sagag.services.gtmotive.builder;

import com.sagag.services.common.utils.XmlUtils;
import com.sagag.services.gtmotive.domain.response.EquipmentItem;
import com.sagag.services.gtmotive.domain.response.GtmotiveBillingCode;
import com.sagag.services.gtmotive.domain.response.GtmotiveEstimate;
import com.sagag.services.gtmotive.domain.response.GtmotiveMake;
import com.sagag.services.gtmotive.domain.response.GtmotiveMakeCode;
import com.sagag.services.gtmotive.domain.response.GtmotiveModel;
import com.sagag.services.gtmotive.domain.response.GtmotiveOperation;
import com.sagag.services.gtmotive.domain.response.GtmotiveSimpleAttribute;
import com.sagag.services.gtmotive.domain.response.GtmotiveUmc;
import com.sagag.services.gtmotive.domain.response.GtmotiveUserInfo;
import com.sagag.services.gtmotive.domain.response.GtmotiveVehicleInfo;
import com.sagag.services.gtmotive.domain.response.GtmotiveVin;
import com.sagag.services.gtmotive.domain.response.OperationPrecalculation;
import com.sagag.services.gtmotive.domain.response.RegistrationNumber;
import com.sagag.services.gtmotive.dto.response.gtinterface.GtmotiveVehicleInfoResponse;
import com.sagag.services.gtmotive.exception.GtmotiveXmlResponseProcessingException;

import lombok.extern.slf4j.Slf4j;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

@Slf4j
public class GraphicalSelectedPartsResponseBuilder {

  private static final String ATTR_CURRENCY = "currency";

  private static final String ATTR_CULTURE = "culture";

  private static final String ATTR_LOCKED = "locked";

  private String unescapseXmlData;

  public GraphicalSelectedPartsResponseBuilder unescapseXmlData(final String unescapseXmlData) {
    this.unescapseXmlData = unescapseXmlData;
    return this;
  }

  public GtmotiveVehicleInfoResponse build() throws GtmotiveXmlResponseProcessingException {
    final Optional<Document> doc = XmlUtils.createXmlDocument(unescapseXmlData,StandardCharsets.UTF_8);
    if (!doc.isPresent()) {
      throw new GtmotiveXmlResponseProcessingException("Invalid XML response");
    }
    return transferFromDocument(doc.get());
  }

  private static GtmotiveVehicleInfoResponse transferFromDocument(final Document doc)
    throws GtmotiveXmlResponseProcessingException {

    final GtmotiveVehicleInfoResponse response = new GtmotiveVehicleInfoResponse();
    try {
      final XPathFactory xPathfactory = XPathFactory.newInstance();
      final XPath xpath = xPathfactory.newXPath();
      response.setEstimateId(buildEstimateId(doc, xpath));
      response.setUserInfo(buildUserInfo(doc, xpath));
      response.setVehicleInfo(buildVehicleInfo(doc, xpath));
      final XPathExpression operationItemsPath = xpath.compile("/estimate/operationList/operation");
      final NodeList operationItemsNodeList =
        (NodeList) operationItemsPath.evaluate(doc, XPathConstants.NODESET);
      response.setOperations(IntStream.range(0, operationItemsNodeList.getLength()).boxed()
        .map(i -> buildOperationItem((Element) operationItemsNodeList.item(i)))
        .collect(Collectors.toList()));
    } catch (XPathExpressionException xpathEx) {
      log.error("Error while building the response from XML", xpathEx);
      throw new GtmotiveXmlResponseProcessingException("Xpath expression error", xpathEx);
    }
    return response;
  }

  private static GtmotiveOperation buildOperationItem(final Element item) {
    final GtmotiveOperation operationItem = new GtmotiveOperation();
    final Node shortNumberNode = item.getElementsByTagName("shortNumber").item(0);
    if (shortNumberNode != null) {
      operationItem.setShortNumber(shortNumberNode.getFirstChild().getNodeValue());
    }
    final Node partNumberNode = item.getElementsByTagName("partNumber").item(0);
    if (partNumberNode != null) {
      operationItem.setPartNumber(partNumberNode.getFirstChild().getNodeValue());
    }
    final Node typeNode = item.getElementsByTagName("type").item(0);
    if (typeNode != null) {
      operationItem.setType(typeNode.getFirstChild().getNodeValue());
    }
    final Node materialTypeNode = item.getElementsByTagName("materialType").item(0);
    if (materialTypeNode != null) {
      operationItem.setMaterialType(materialTypeNode.getFirstChild().getNodeValue());
    }
    final Node actionDescNode = item.getElementsByTagName("actionDescription").item(0);
    if (actionDescNode != null) {
      operationItem.setAction(buildSimpleAttribute((Element) actionDescNode));
    }
    final Node operationsChildsNode = item.getElementsByTagName("operationsChilds").item(0);
    if (operationsChildsNode != null) {
      operationItem.setChilds(buildOperationItemChilds((Element) operationsChildsNode));
    }
    final Node depreciationNode = item.getElementsByTagName("depreciation").item(0);
    if (depreciationNode != null) {
      operationItem.setDepreciation(Integer
        .valueOf(depreciationNode.getFirstChild().getNodeValue()));
    }
    final Node descriptionNode = item.getElementsByTagName("description").item(0);
    if (descriptionNode != null) {
      operationItem.setDescription(buildSimpleAttribute((Element) descriptionNode));
    }
    final Node isMultiReferenceNode = item.getElementsByTagName("isMultiReference").item(0);
    if (isMultiReferenceNode != null) {
      operationItem.setMultiReference(Boolean.valueOf(isMultiReferenceNode.getFirstChild()
        .getNodeValue()));
    }
    final Node operationStateNode = item.getElementsByTagName("operationState").item(0);
    if (operationStateNode != null) {
      operationItem.setOperationState(operationStateNode.getFirstChild().getNodeValue());
    }
    final Node precalculationNode = item.getElementsByTagName("precalculation").item(0);
    if (precalculationNode != null) {
      operationItem.setPrecalculation(buildPrecalculation((Element) precalculationNode));
    }
    final Node preventiveTypeNode = item.getElementsByTagName("preventiveType").item(0);
    if (preventiveTypeNode != null) {
      operationItem.setPreventiveType(preventiveTypeNode.getFirstChild().getNodeValue());
    }
    final Node referenceNode = item.getElementsByTagName("reference").item(0);
    if (referenceNode != null) {
      operationItem.setReference(referenceNode.getFirstChild().getNodeValue());
    }
    final Node viewPrecalculationNode = item.getElementsByTagName("viewPrecalculation").item(0);
    if (viewPrecalculationNode != null) {
      operationItem.setViewPrecalculation(Boolean.valueOf(viewPrecalculationNode.getFirstChild()
        .getNodeValue()));
    }
    return operationItem;
  }

  private static OperationPrecalculation buildPrecalculation(final Element element) {
    final OperationPrecalculation precalculation = new OperationPrecalculation();
    precalculation.setCode(element.getElementsByTagName("code").item(0).getFirstChild()
      .getNodeValue());
    precalculation.setUnits(Double.valueOf(element.getElementsByTagName("units").item(0)
      .getFirstChild().getNodeValue()));
    return precalculation;
  }

  private static List<GtmotiveOperation> buildOperationItemChilds(final Element element) {
    if (!element.hasChildNodes()) {
      return Collections.emptyList();
    }
    final NodeList childNodeList = element.getChildNodes();
    return IntStream.range(0, childNodeList.getLength()).boxed()
      .map(i -> buildOperationItem((Element) childNodeList.item(i))).collect(Collectors.toList());
  }

  private static GtmotiveSimpleAttribute buildSimpleAttribute(final Element element) {
    final GtmotiveSimpleAttribute actionDesc = new GtmotiveSimpleAttribute();
    final Node idNode = element.getElementsByTagName("id").item(0);
    actionDesc.setId(idNode.getFirstChild().getNodeValue());
    final Node valueNode = element.getElementsByTagName("value").item(0);
    actionDesc.setValue(valueNode.getFirstChild().getNodeValue());
    return actionDesc;
  }

  private static GtmotiveVehicleInfo buildVehicleInfo(final Document doc, final XPath xpath)
    throws XPathExpressionException {
    final GtmotiveVehicleInfo vehicleInfo = new GtmotiveVehicleInfo();
    final XPathExpression commercialModelIdPath =
      xpath.compile("/estimate/vehicleInfo/commercialModelId/text()");
    vehicleInfo.setCommercialModelId(Integer.valueOf((String) commercialModelIdPath.evaluate(doc,
      XPathConstants.STRING)));
    vehicleInfo.setEquipments(buildEquipments(doc, xpath));
    vehicleInfo.setRanks(buildRanks(doc, xpath));
    vehicleInfo.setMake(buildMake(doc, xpath));
    vehicleInfo.setMakeCode(buildMakeCode(doc, xpath));
    vehicleInfo.setModel(buildModel(doc, xpath));
    final XPathExpression modelTypePath = xpath.compile("/estimate/vehicleInfo/modelType/text()");
    vehicleInfo.setModelType((String) modelTypePath.evaluate(doc, XPathConstants.STRING));
    vehicleInfo.setRegistrationNr(buildRegistrationNr(doc, xpath));
    vehicleInfo.setUmc(buildUmc(doc, xpath));
    vehicleInfo.setVin(buildVin(doc, xpath));
    return vehicleInfo;
  }

  private static GtmotiveVin buildVin(final Document doc, final XPath xpath)
    throws XPathExpressionException {
    final GtmotiveVin vin = new GtmotiveVin();
    vin.setLocked((Boolean) xpath.compile("/estimate/vehicleInfo/vin/@locked").evaluate(doc,
      XPathConstants.BOOLEAN));
    vin.setValue((String) xpath.compile("/estimate/vehicleInfo/vin/value/text()").evaluate(doc,
      XPathConstants.STRING));
    return vin;
  }

  private static GtmotiveUmc buildUmc(final Document doc, final XPath xpath)
    throws XPathExpressionException {
    final GtmotiveUmc umc = new GtmotiveUmc();
    umc.setLocked((Boolean) xpath.compile("/estimate/vehicleInfo/umc/@locked").evaluate(doc,
      XPathConstants.BOOLEAN));
    umc.setValue((String) xpath.compile("/estimate/vehicleInfo/umc/value/text()").evaluate(doc,
      XPathConstants.STRING));
    return umc;
  }

  private static RegistrationNumber buildRegistrationNr(final Document doc, final XPath xpath)
    throws XPathExpressionException {
    final RegistrationNumber registrationNumber = new RegistrationNumber();
    registrationNumber.setLocked((Boolean) xpath.compile(
      "/estimate/vehicleInfo/registrationNumber/@locked").evaluate(doc, XPathConstants.BOOLEAN));
    registrationNumber.setValue((String) xpath.compile(
      "/estimate/vehicleInfo/registrationNumber/value/text()").evaluate(doc,
      XPathConstants.STRING));
    return registrationNumber;
  }

  private static GtmotiveModel buildModel(final Document doc, final XPath xpath)
    throws XPathExpressionException {
    final GtmotiveModel model = new GtmotiveModel();
    model.setLocked((Boolean) xpath.compile("/estimate/vehicleInfo/model/@locked").evaluate(doc,
      XPathConstants.BOOLEAN));
    model.setValue((String) xpath.compile("/estimate/vehicleInfo/model/value/value/text()")
      .evaluate(doc, XPathConstants.STRING));
    return model;
  }

  private static GtmotiveMakeCode buildMakeCode(final Document doc, final XPath xpath)
    throws XPathExpressionException {
    final GtmotiveMakeCode makeCode = new GtmotiveMakeCode();
    makeCode.setLocked((Boolean) xpath.compile("/estimate/vehicleInfo/makeCode/@locked").evaluate(
      doc, XPathConstants.BOOLEAN));
    makeCode.setValue((String) xpath.compile("/estimate/vehicleInfo/makeCode/value/text()")
      .evaluate(doc, XPathConstants.STRING));
    return makeCode;
  }

  private static GtmotiveMake buildMake(final Document doc, final XPath xpath)
    throws XPathExpressionException {
    final GtmotiveMake make = new GtmotiveMake();
    make.setLocked((Boolean) xpath.compile("/estimate/vehicleInfo/make/@locked").evaluate(doc,
      XPathConstants.BOOLEAN));
    make.setValue((String) xpath.compile("/estimate/vehicleInfo/make/value/value/text()").evaluate(
      doc, XPathConstants.STRING));
    return make;
  }

  private static List<EquipmentItem> buildEquipments(final Document doc, final XPath xpath)
    throws XPathExpressionException {
    final XPathExpression itemValuesPath =
      xpath.compile("/estimate/vehicleInfo/equipmentList/item/value/text()");
    final XPathExpression itemDescriptionPath =
      xpath.compile("/estimate/vehicleInfo/equipmentList/item/description/value/text()");
    final XPathExpression itemFamilyPath =
      xpath.compile("/estimate/vehicleInfo/equipmentList/item/family/text()");
    final XPathExpression itemFamilyDescPath =
      xpath.compile("/estimate/vehicleInfo/equipmentList/item/familyDescription/value/text()");
    final XPathExpression itemSubFamilyPath =
      xpath.compile("/estimate/vehicleInfo/equipmentList/item/subFamily/text()");
    final XPathExpression itemSubFamilyDescPath =
      xpath.compile("/estimate/vehicleInfo/equipmentList/item/subFamilyDescription/value/text()");

    final NodeList itemValuesNodeList =
      (NodeList) itemValuesPath.evaluate(doc, XPathConstants.NODESET);
    final NodeList itemDescNodeList =
      (NodeList) itemDescriptionPath.evaluate(doc, XPathConstants.NODESET);
    final NodeList itemFamilyNodeList =
      (NodeList) itemFamilyPath.evaluate(doc, XPathConstants.NODESET);
    final NodeList itemFamilyDescNodeList =
      (NodeList) itemFamilyDescPath.evaluate(doc, XPathConstants.NODESET);
    final NodeList itemSubFamilyNodeList =
      (NodeList) itemSubFamilyPath.evaluate(doc, XPathConstants.NODESET);
    final NodeList itemSubFamilyDescNodeList =
      (NodeList) itemSubFamilyDescPath.evaluate(doc, XPathConstants.NODESET);

    final int equipmentItemsLength = itemValuesNodeList.getLength();
    final List<EquipmentItem> equipmentItems = new ArrayList<>(equipmentItemsLength);
    IntStream.range(0, equipmentItemsLength).boxed().forEach(i -> {
      final EquipmentItem equipmentItem = new EquipmentItem();
      equipmentItem.setDescription(itemDescNodeList.item(i).getNodeValue());
      equipmentItem.setFamily(itemFamilyNodeList.item(i).getNodeValue());
      equipmentItem.setFamilyDescription(itemFamilyDescNodeList.item(i).getNodeValue());
      equipmentItem.setSubFamily(itemSubFamilyNodeList.item(i).getNodeValue());
      equipmentItem.setSubFamilyDescription(itemSubFamilyDescNodeList.item(i).getNodeValue());
      equipmentItem.setValue(itemValuesNodeList.item(i).getNodeValue());
      equipmentItems.add(equipmentItem);
    });
    return equipmentItems;
  }

  private static List<EquipmentItem> buildRanks(final Document doc, final XPath xpath)
    throws XPathExpressionException {
    final XPathExpression itemValuesPath =
      xpath.compile("/estimate/vehicleInfo/equipmentList/rank/value/text()");
    final XPathExpression itemFamilyPath =
      xpath.compile("/estimate/vehicleInfo/equipmentList/rank/family/text()");
    final XPathExpression itemFamilyDescPath =
      xpath.compile("/estimate/vehicleInfo/equipmentList/rank/familyDescription/value/text()");
    final XPathExpression itemSubFamilyPath =
      xpath.compile("/estimate/vehicleInfo/equipmentList/rank/subFamily/text()");
    final XPathExpression itemSubFamilyDescPath =
      xpath.compile("/estimate/vehicleInfo/equipmentList/rank/subFamilyDescription/value/text()");

    final NodeList itemValuesNodeList =
      (NodeList) itemValuesPath.evaluate(doc, XPathConstants.NODESET);
    final NodeList itemFamilyNodeList =
      (NodeList) itemFamilyPath.evaluate(doc, XPathConstants.NODESET);
    final NodeList itemFamilyDescNodeList =
      (NodeList) itemFamilyDescPath.evaluate(doc, XPathConstants.NODESET);
    final NodeList itemSubFamilyNodeList =
      (NodeList) itemSubFamilyPath.evaluate(doc, XPathConstants.NODESET);
    final NodeList itemSubFamilyDescNodeList =
      (NodeList) itemSubFamilyDescPath.evaluate(doc, XPathConstants.NODESET);

    final int equipmentItemsLength = itemValuesNodeList.getLength();
    final List<EquipmentItem> equipmentItems = new ArrayList<>(equipmentItemsLength);
    IntStream.range(0, equipmentItemsLength).boxed().forEach(i -> {
      final EquipmentItem equipmentItem = new EquipmentItem();
      equipmentItem.setFamily(itemFamilyNodeList.item(i).getNodeValue());
      equipmentItem.setFamilyDescription(itemFamilyDescNodeList.item(i).getNodeValue());
      equipmentItem.setSubFamily(itemSubFamilyNodeList.item(i).getNodeValue());
      equipmentItem.setSubFamilyDescription(itemSubFamilyDescNodeList.item(i).getNodeValue());
      equipmentItem.setValue(itemValuesNodeList.item(i).getNodeValue());
      equipmentItems.add(equipmentItem);
    });

    return equipmentItems;
  }

  private static GtmotiveUserInfo buildUserInfo(final Document doc, final XPath xpath)
    throws XPathExpressionException {
    final GtmotiveUserInfo userInfo = new GtmotiveUserInfo();
    final XPathExpression userInfoPath = xpath.compile("/estimate/userInfo");
    final Element userInfoEle = (Element) userInfoPath.evaluate(doc, XPathConstants.NODE);
    userInfo.setCulture(userInfoEle.getAttribute(ATTR_CULTURE));
    userInfo.setCurrency(userInfoEle.getAttribute(ATTR_CURRENCY));
    userInfo.setBillingCode(buildBillingCode(doc, xpath));
    userInfo.setCountry(buildCountry(doc, xpath));
    userInfo.setCountryIsoCode((String) xpath.compile("/estimate/userInfo/countryIsoCode/text()")
      .evaluate(doc, XPathConstants.STRING));
    userInfo.setCultureIsoCode((String) xpath.compile("/estimate/userInfo/cultureIsoCode/text()")
      .evaluate(doc, XPathConstants.STRING));
    userInfo.setCurrencyIsoCode((String) xpath.compile("/estimate/userInfo/currencyIsoCode/text()")
      .evaluate(doc, XPathConstants.STRING));
    userInfo.setLanguage(buildLanguage(doc, xpath));
    return userInfo;
  }

  private static GtmotiveSimpleAttribute buildLanguage(Document doc, XPath xpath)
    throws XPathExpressionException {
    final GtmotiveSimpleAttribute language = new GtmotiveSimpleAttribute();
    final XPathExpression idPath = xpath.compile("/estimate/userInfo/language/id");
    language.setId((String) idPath.evaluate(doc, XPathConstants.STRING));
    final XPathExpression valuePath = xpath.compile("/estimate/userInfo/language/value");
    language.setValue((String) valuePath.evaluate(doc, XPathConstants.STRING));
    return language;
  }

  private static GtmotiveSimpleAttribute buildCountry(final Document doc, final XPath xpath)
    throws XPathExpressionException {
    final GtmotiveSimpleAttribute country = new GtmotiveSimpleAttribute();
    final XPathExpression idPath = xpath.compile("/estimate/userInfo/country/id");
    country.setId((String) idPath.evaluate(doc, XPathConstants.STRING));
    final XPathExpression valuePath = xpath.compile("/estimate/userInfo/country/value");
    country.setValue((String) valuePath.evaluate(doc, XPathConstants.STRING));
    return country;
  }

  private static GtmotiveBillingCode buildBillingCode(final Document doc, final XPath xpath)
    throws XPathExpressionException {
    final GtmotiveBillingCode billingCode = new GtmotiveBillingCode();
    final XPathExpression billingCodePath = xpath.compile("/estimate/userInfo/billingCode");
    final Element billingCodePathEle = (Element) billingCodePath.evaluate(doc, XPathConstants.NODE);
    billingCode.setLocked(Boolean.valueOf(billingCodePathEle.getAttribute(ATTR_LOCKED)));
    final XPathExpression codePath =
      xpath.compile("/estimate/userInfo/billingCode/value/code/text()");
    billingCode.setCode((String) codePath.evaluate(doc, XPathConstants.STRING));
    final XPathExpression descPath =
      xpath.compile("/estimate/userInfo/billingCode/value/description/text()");
    billingCode.setDescription((String) descPath.evaluate(doc, XPathConstants.STRING));
    return billingCode;
  }

  private static GtmotiveEstimate buildEstimateId(final Document doc, final XPath xpath)
    throws XPathExpressionException {
    final XPathExpression estimateIdPath = xpath.compile("/estimate/estimateId");
    final GtmotiveEstimate estimate = new GtmotiveEstimate();
    final Element nodeEle = (Element) estimateIdPath.evaluate(doc, XPathConstants.NODE);
    estimate.setLocked(Boolean.valueOf(nodeEle.getAttribute(ATTR_LOCKED)));
    estimate.setId(nodeEle.getTextContent());
    return estimate;
  }

}
