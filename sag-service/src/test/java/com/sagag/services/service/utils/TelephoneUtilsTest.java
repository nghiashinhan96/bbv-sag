package com.sagag.services.service.utils;

import com.sagag.services.elasticsearch.criteria.Telephone;
import com.sagag.services.service.exception.TelephoneFormatException;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

/**
 * Class test for Telephone utils.
 */
@RunWith(MockitoJUnitRunner.class)
public class TelephoneUtilsTest {

  private static final String CC_43 = "43";

  @Test
  public void givenTelephone_shouldMatchRegex() {
    // +437237 / 2265
    // +43(07260) 4967
    // 004372372265
    // 072372265
    Assert.assertThat(true, Matchers.is(TelephoneUtils.matchesRegex("+437237 / 2265")));
    Assert.assertThat(true, Matchers.is(TelephoneUtils.matchesRegex("+43(07260) 4967")));
    Assert.assertThat(true, Matchers.is(TelephoneUtils.matchesRegex("+43(07260) 4 9(6)7")));
    Assert.assertThat(true, Matchers.is(TelephoneUtils.matchesRegex("+417237 / 2265")));
    Assert.assertThat(true, Matchers.is(TelephoneUtils.matchesRegex("004372372265")));
    Assert.assertThat(true, Matchers.is(TelephoneUtils.matchesRegex("0043(07260) 4967")));
    Assert.assertThat(true, Matchers.is(TelephoneUtils.matchesRegex("00437237 / 2265")));
    Assert.assertThat(true, Matchers.is(TelephoneUtils.matchesRegex("00437237 / 22  6 / () 5")));
    Assert.assertThat(true, Matchers.is(TelephoneUtils.matchesRegex("072372265")));
    Assert.assertThat(true, Matchers.is(TelephoneUtils.matchesRegex("07237 / 2265")));
    Assert.assertThat(false, Matchers.is(TelephoneUtils.matchesRegex("72372265")));
    Assert.assertThat(false, Matchers.is(TelephoneUtils.matchesRegex("+457237 / 2265")));
    Assert.assertThat(true, Matchers.is(TelephoneUtils.matchesRegex("00457237 / 2265")));
    Assert.assertThat(true, Matchers.is(TelephoneUtils.matchesRegex("0004372372265")));
    Assert.assertThat(true, Matchers.is(TelephoneUtils.matchesRegex("0072372265")));
  }

  @Test
  public void givenTelephone_shouldGetCountryCode() throws TelephoneFormatException {
    Optional<Telephone> telOpt = TelephoneUtils.extract("+437237 / 2265");
    Assert.assertThat(true, Matchers.is(telOpt.isPresent()));
    Telephone tel = telOpt.get();
    Assert.assertThat(CC_43, Matchers.is(tel.getCountryCode()));
    Assert.assertThat("72372265", Matchers.is(tel.getNumber()));
    
    telOpt = TelephoneUtils.extract("+43(07260) 4967");
    Assert.assertThat(true, Matchers.is(telOpt.isPresent()));
    tel = telOpt.get();
    Assert.assertThat(CC_43, Matchers.is(tel.getCountryCode()));
    Assert.assertThat("72604967", Matchers.is(tel.getNumber()));
    
    telOpt = TelephoneUtils.extract("+43(07260) 4 9(6)7");
    Assert.assertThat(true, Matchers.is(telOpt.isPresent()));
    tel = telOpt.get();
    Assert.assertThat(CC_43, Matchers.is(tel.getCountryCode()));
    Assert.assertThat("72604967", Matchers.is(tel.getNumber()));
    
    telOpt = TelephoneUtils.extract("+417437 / 2265");
    Assert.assertThat(true, Matchers.is(telOpt.isPresent()));
    tel = telOpt.get();
    Assert.assertThat("41", Matchers.is(tel.getCountryCode()));
    Assert.assertThat("74372265", Matchers.is(tel.getNumber()));
    
    telOpt = TelephoneUtils.extract("004372372265");
    Assert.assertThat(true, Matchers.is(telOpt.isPresent()));
    tel = telOpt.get();
    Assert.assertThat(CC_43, Matchers.is(tel.getCountryCode()));
    Assert.assertThat("72372265", Matchers.is(tel.getNumber()));
    
    telOpt = TelephoneUtils.extract("0043(07260) 4967");
    Assert.assertThat(true, Matchers.is(telOpt.isPresent()));
    tel = telOpt.get();
    Assert.assertThat(CC_43, Matchers.is(tel.getCountryCode()));
    Assert.assertThat("72604967", Matchers.is(tel.getNumber()));
    
    telOpt = TelephoneUtils.extract("00437237 / 2265");
    Assert.assertThat(true, Matchers.is(telOpt.isPresent()));
    tel = telOpt.get();
    Assert.assertThat(CC_43, Matchers.is(tel.getCountryCode()));
    Assert.assertThat("72372265", Matchers.is(tel.getNumber()));
    
    telOpt = TelephoneUtils.extract("00437237 / 22  6 / () 5");
    Assert.assertThat(true, Matchers.is(telOpt.isPresent()));
    tel = telOpt.get();
    Assert.assertThat(CC_43, Matchers.is(tel.getCountryCode()));
    Assert.assertThat("72372265", Matchers.is(tel.getNumber()));
    
    telOpt = TelephoneUtils.extract("072372265");
    Assert.assertThat(true, Matchers.is(telOpt.isPresent()));
    tel = telOpt.get();
    Assert.assertThat(StringUtils.EMPTY, Matchers.is(tel.getCountryCode()));
    Assert.assertThat("72372265", Matchers.is(tel.getNumber()));
    
    telOpt = TelephoneUtils.extract("07237 / 2265");
    Assert.assertThat(true, Matchers.is(telOpt.isPresent()));
    tel = telOpt.get();
    Assert.assertThat(StringUtils.EMPTY, Matchers.is(tel.getCountryCode()));
    Assert.assertThat("72372265", Matchers.is(tel.getNumber()));
    
  }

  @Test(expected = TelephoneFormatException.class)
  public void givenInvalidTelephone_shouldGetException() throws TelephoneFormatException {
    Optional<Telephone> telOpt = TelephoneUtils.extract("72372265");
    Assert.assertThat(false, Matchers.is(telOpt.isPresent()));
  }

  @Test(expected = TelephoneFormatException.class)
  public void givenInvalidTelephone_shouldGetException2() throws TelephoneFormatException {
    Optional<Telephone> telOpt = TelephoneUtils.extract("+457237 / 2265");
    Assert.assertThat(false, Matchers.is(telOpt.isPresent()));
  }
  
  @Test(expected = TelephoneFormatException.class)
  public void givenInvalidTelephone_shouldGetException3() throws TelephoneFormatException {
    Optional<Telephone> telOpt = TelephoneUtils.extract("00457237 / 2265");
    Assert.assertThat(false, Matchers.is(telOpt.isPresent()));
  }
  
  @Test(expected = TelephoneFormatException.class)
  public void givenInvalidTelephone_shouldGetException4() throws TelephoneFormatException {
    Optional<Telephone> telOpt = TelephoneUtils.extract("0004372372265");
    Assert.assertThat(false, Matchers.is(telOpt.isPresent()));
  }
  
  @Test(expected = TelephoneFormatException.class)
  public void givenInvalidTelephone_shouldGetException5() throws TelephoneFormatException {
    Optional<Telephone> telOpt = TelephoneUtils.extract("0072372265");
    Assert.assertThat(false, Matchers.is(telOpt.isPresent()));
  }
}
