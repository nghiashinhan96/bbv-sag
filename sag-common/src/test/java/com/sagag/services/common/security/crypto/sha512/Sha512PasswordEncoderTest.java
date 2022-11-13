package com.sagag.services.common.security.crypto.sha512;

import com.sagag.services.common.utils.HashUtils;

import org.apache.commons.codec.DecoderException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class Sha512PasswordEncoderTest {

  @InjectMocks
  private Sha512PasswordEncoder pwdEncoder;

  @Test
  public void testMatchingSha512_shouldMatch() throws DecoderException {
    final String raw = "1234";
    final String hash =
        "0x0F9F63321A8012E33B3D70D942F018B6FEC3DDDBCEED6FA6250C8CAE860408C835B5282B8E6E9FC91B048724BB8F6B08809BBAD6BA9B77A87466F0952CA74F1E";
    final String salt =
        "0x580E7BD8106CCB2D3D4179C683CC79E208E01C120DC4D1B7EFCE27561B534556F9EA06F5EC74A2F1D05BB5195A20C36B37EFB3A74CBEC9FCEE929F5D01BA840A7235F4CEBBB4E75052439CF91DA16995964C583FA1B98A70E984EBF70741837809F1150BFF4DA088B41304CF3EF69C9C43D2C526CEAE74EF789B28CDFE1EFA58";
    final String encodedPasswordAndSaltAsString =
        HashUtils.getEncodedPasswordAndSaltAsString(hash, salt);
    final boolean isMatched = pwdEncoder.matches(raw, encodedPasswordAndSaltAsString);
    Assert.assertTrue(isMatched);
  }

}
