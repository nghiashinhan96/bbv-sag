package com.sagag.services.gtmotive.builder.mainmoduleservice;

import com.sagag.services.common.utils.XmlUtils;
import com.sagag.services.gtmotive.config.GtmotiveProfile;
import com.sagag.services.gtmotive.dto.response.mainmoduleservice.GtmotivePartsThreeReference;
import com.sagag.services.gtmotive.dto.response.mainmoduleservice.GtmotivePartsThreeResponse;
import com.sagag.services.gtmotive.exception.GtmotiveXmlResponseProcessingException;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

@Component
@GtmotiveProfile
public class GtmotivePartsThreeResponseBuilder {

  private static final String ERROR_EXPR = "/response/error/errorInfo/code/text()";

  @Autowired
  private GtmotivePartsThreeReferencesCaseOneHandler gtmotivePartsThreeReferencesCaseOneHandler;

  @Autowired
  private GtmotivePartsThreeReferencesCaseTwoHandler gtmotivePartsThreeReferencesCaseTwoHandler;

  @Autowired
  private GtmotivePartsThreeReferencesCaseThreeHandler gtmotivePartsThreeReferencesCaseThreeHandler;


  public GtmotivePartsThreeResponse build(final String unescapseXmlData)
      throws GtmotiveXmlResponseProcessingException, XPathExpressionException {

    final Optional<Document> doc =
        XmlUtils.createXmlDocument(unescapseXmlData, StandardCharsets.UTF_16);
    if (!doc.isPresent()) {
      throw new GtmotiveXmlResponseProcessingException("Invalid XML response");
    }
    return transferFromDocument(doc.get());
  }

  private GtmotivePartsThreeResponse transferFromDocument(final Document doc)
      throws XPathExpressionException {
    GtmotivePartsThreeResponse response = new GtmotivePartsThreeResponse();
    final XPathFactory xPathfactory = XPathFactory.newInstance();
    final XPath xpath = xPathfactory.newXPath();
    response.setReferences(buildReferences(doc, xpath));
    response.setErrorCode(buildError(doc, xpath));
    return response;
  }

  private List<GtmotivePartsThreeReference> buildReferences(Document doc, XPath xpath)
      throws XPathExpressionException {
    List<GtmotivePartsThreeReference> references = new ArrayList<>();

    List<GtmotivePartsThreeReference> caseOne =
        gtmotivePartsThreeReferencesCaseOneHandler.achieveReferences(doc, xpath);
    if (CollectionUtils.isNotEmpty(caseOne)) {
      references.addAll(caseOne);
    }

    List<GtmotivePartsThreeReference> caseTwo =
        gtmotivePartsThreeReferencesCaseTwoHandler.achieveReferences(doc, xpath);
    if (CollectionUtils.isNotEmpty(caseTwo)) {
      references.addAll(caseTwo);
    }

    List<GtmotivePartsThreeReference> caseThree =
        gtmotivePartsThreeReferencesCaseThreeHandler.achieveReferences(doc, xpath);
    if (CollectionUtils.isNotEmpty(caseThree)) {
      references.addAll(caseThree);
    }
    return references;
  }

  private Integer buildError(Document doc, XPath xpath) throws XPathExpressionException {
    final Number errorCode =
        (Number) xpath.compile(ERROR_EXPR).evaluate(doc, XPathConstants.NUMBER);
    return Optional.ofNullable(errorCode).map(Number::intValue).orElse(null);
  }
}
