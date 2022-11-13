package com.sagag.services.gtmotive.builder.mainmoduleservice;

import com.sagag.services.gtmotive.config.GtmotiveProfile;
import com.sagag.services.gtmotive.dto.response.mainmoduleservice.GtmotivePartsThreeReference;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

@Component
@GtmotiveProfile
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class GtmotivePartsThreeReferencesCaseTwoHandler
    extends AbstractGtmotivePartsThreeReferencesHandler implements InitializingBean {

  private String newMultiReference;
  private String standardMultiReference;
  private String economicMutiReference;

  private boolean checkNewReference(Document doc, XPath xpath) throws XPathExpressionException {
    final XPathExpression newReferenceExpr = xpath.compile(newMultiReference);
    NodeList nodeList = (NodeList) newReferenceExpr.evaluate(doc, XPathConstants.NODESET);
    return !Objects.isNull(nodeList) && nodeList.getLength() > 0;
  }

  private boolean checkStandardReference(Document doc, XPath xpath)
      throws XPathExpressionException {
    final XPathExpression standarReferenceExpr = xpath.compile(standardMultiReference);
    NodeList nodeList = (NodeList) standarReferenceExpr.evaluate(doc, XPathConstants.NODESET);
    return !Objects.isNull(nodeList) && nodeList.getLength() > 0;
  }

  private boolean checkEconomicReference(Document doc, XPath xpath)
      throws XPathExpressionException {
    final XPathExpression standarReferenceExpr = xpath.compile(economicMutiReference);
    NodeList nodeList = (NodeList) standarReferenceExpr.evaluate(doc, XPathConstants.NODESET);
    return !Objects.isNull(nodeList) && nodeList.getLength() > 0;
  }

  @Override
  protected List<GtmotivePartsThreeReference> achieveReferences(Document doc, XPath xpath)
      throws XPathExpressionException {
    if (checkNewReference(doc, xpath)) {
      return achieveReferences(newMultiReference, doc, xpath);
    }
    if (checkStandardReference(doc, xpath)) {
      return achieveReferences(standardMultiReference, doc, xpath);
    }
    if (checkEconomicReference(doc, xpath)) {
      return achieveReferences(economicMutiReference, doc, xpath);
    }
    return Collections.emptyList();
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    newMultiReference =
        "/response/requestedData/partDetails/reference/multiReferences/multiReference[supplyType[text()='"
            + getSupplyTypeNew() + "']]";
    standardMultiReference =
        "/response/requestedData/partDetails/reference/multiReferences/multiReference[supplyType[text()='"
            + getSupplyTypeStandard() + "']]";
    economicMutiReference =
        "/response/requestedData/partDetails/reference/multiReferences/multiReference[supplyType[text()='"
            + getSupplyTypeEconomic() + "']]";
  }
}
