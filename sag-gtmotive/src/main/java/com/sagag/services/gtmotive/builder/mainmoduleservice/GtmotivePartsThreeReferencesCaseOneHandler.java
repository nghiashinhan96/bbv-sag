package com.sagag.services.gtmotive.builder.mainmoduleservice;

import com.sagag.services.gtmotive.config.GtmotiveProfile;
import com.sagag.services.gtmotive.dto.response.mainmoduleservice.GtmotivePartsThreeReference;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

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
public class GtmotivePartsThreeReferencesCaseOneHandler
    extends AbstractGtmotivePartsThreeReferencesHandler implements InitializingBean {

  @Autowired
  private GtmotivePartsThreeReferencesCaseThreeHandler gtmotivePartsThreeReferencesCaseThreeHandler;

  private String newMutiReference;
  private String standardMutiReference;
  private String economicMutiReference;

  private boolean checkNewReference(Document doc, XPath xpath) throws XPathExpressionException {
    final XPathExpression newReferenceExpr = xpath.compile(newMutiReference);
    Node node = (Node) newReferenceExpr.evaluate(doc, XPathConstants.NODE);
    return Objects.nonNull(node) && !gtmotivePartsThreeReferencesCaseThreeHandler.check(doc, xpath);
  }

  private boolean checkStandardReference(Document doc, XPath xpath)
      throws XPathExpressionException {
    final XPathExpression standarReferenceExpr = xpath.compile(standardMutiReference);
    Node node = (Node) standarReferenceExpr.evaluate(doc, XPathConstants.NODE);
    return Objects.nonNull(node) && !gtmotivePartsThreeReferencesCaseThreeHandler.check(doc, xpath);
  }

  private boolean checkEconomicReference(Document doc, XPath xpath)
      throws XPathExpressionException {
    final XPathExpression economicReferenceExpr = xpath.compile(economicMutiReference);
    Node node = (Node) economicReferenceExpr.evaluate(doc, XPathConstants.NODE);
    return Objects.nonNull(node) && !gtmotivePartsThreeReferencesCaseThreeHandler.check(doc, xpath);
  }

  @Override
  protected List<GtmotivePartsThreeReference> achieveReferences(Document doc, XPath xpath)
      throws XPathExpressionException {
    if (checkNewReference(doc, xpath)) {
      return achieveReferences(newMutiReference, doc, xpath);
    }
    if (checkStandardReference(doc, xpath)) {
      return achieveReferences(standardMutiReference, doc, xpath);
    }
    if (checkEconomicReference(doc, xpath)) {
      return achieveReferences(economicMutiReference, doc, xpath);
    }
    return Collections.emptyList();
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    newMutiReference = "/response/requestedData/partDetails/reference[supplyType[text()='"
        + getSupplyTypeNew() + "']]";
    standardMutiReference = "/response/requestedData/partDetails/reference[supplyType[text()='"
        + getSupplyTypeStandard() + "']]";
    economicMutiReference = "/response/requestedData/partDetails/reference[supplyType[text()='"
        + getSupplyTypeEconomic() + "']]";
  }
}
