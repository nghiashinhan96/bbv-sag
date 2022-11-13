package com.sagag.services.copydb;

import org.apache.commons.lang3.StringUtils;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;

import com.ulisesbocchio.jasyptspringboot.EncryptablePropertyDetector;

@SpringBootApplication(scanBasePackages = { "com.sagag.services.copydb" })
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class })
public class SagCopydbApplication {

  public static void main(String[] args) {
    SpringApplication.run(SagCopydbApplication.class, args);
  }

  /**
   * Custom Encryptor bean instance.
   * 
   * @return <code>StringEncryptor</code> instance.
   */
  @Bean("copydbPwEncryptorBean")
  public StringEncryptor copydbPwEncryptorBean() {
    final PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
    final SimpleStringPBEConfig config = new SimpleStringPBEConfig();
    config.setPassword("B92V.zd^2FcFT9r");
    config.setAlgorithm("PBEWithMD5AndDES");
    config.setKeyObtentionIterations("1000");
    config.setPoolSize("1");
    config.setProviderName("SunJCE");
    config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
    config.setIvGeneratorClassName("org.jasypt.iv.NoIvGenerator");
    config.setStringOutputType("base64");
    encryptor.setConfig(config);
    return encryptor;
  }

  /**
   * Detector bean for encryptable property.
   * 
   * @return <code>EncryptablePropertyDetector</code> instance.
   */
  @Bean(name = "encryptablePropertyDetector")
  public EncryptablePropertyDetector encryptablePropertyDetector() {
    return new ConnectEncryptablePropertyDetector();
  }

  private static class ConnectEncryptablePropertyDetector implements EncryptablePropertyDetector {

    private static final String PW_PREFIX = "ENC@[";

    @Override
    public boolean isEncrypted(String value) {
      if (StringUtils.isBlank(value)) {
        return false;
      }
      return value.startsWith(PW_PREFIX);
    }

    @Override
    public String unwrapEncryptedValue(String value) {
      if (StringUtils.isBlank(value)) {
        throw new IllegalArgumentException("Password must not be empty.");
      }
      return value.substring(PW_PREFIX.length());
    }
  }
}
