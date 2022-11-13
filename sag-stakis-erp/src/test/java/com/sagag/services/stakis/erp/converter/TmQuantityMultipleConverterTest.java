package com.sagag.services.stakis.erp.converter;

import java.io.IOException;
import java.util.Optional;

import javax.xml.bind.JAXBException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringRunner;

import com.sagag.services.stakis.erp.converter.impl.article.TmQuantityMultipleConverter;
import com.sagag.services.stakis.erp.wsdl.tmconnect.ErpInformation;

@RunWith(SpringRunner.class)
public class TmQuantityMultipleConverterTest {

  @InjectMocks
  private TmQuantityMultipleConverter converter;

  @Test
  public void shouldTmQuantityMultipleConverter() throws IOException, JAXBException {

	  ErpInformation erpInformation = new ErpInformation();
	  Optional<Integer> result = converter.apply(erpInformation);
	  Assert.assertFalse(result.isPresent());
  }

}
