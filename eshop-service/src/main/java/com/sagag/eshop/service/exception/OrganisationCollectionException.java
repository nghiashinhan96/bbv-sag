package com.sagag.eshop.service.exception;
import com.sagag.services.common.exception.IBusinessCode;
import com.sagag.services.common.exception.ValidationException;

import lombok.Getter;

import java.util.function.Supplier;

/**
 * Generic exception class for Organisation Collection.
 */
@Getter
public class OrganisationCollectionException extends ValidationException {
  private static final long serialVersionUID = 1240495533695233085L;

  public OrganisationCollectionException(OrganisationCollectionErrorCase error, String message) {
    super(message);
    setCode(error.code());
    setKey(error.key());
  }

  public static Supplier<OrganisationCollectionException> collectionNotFound(String shortName) {
    return () -> new OrganisationCollectionException(OrganisationCollectionErrorCase.OCE_NFC_001,
        "cannot found collection: " + shortName);
  }

  public static Supplier<OrganisationCollectionException> affiliateNotFound() {
    return () -> new OrganisationCollectionException(OrganisationCollectionErrorCase.OCE_NFA_001,
        "cannot found affiliate");
  }

  public static Supplier<OrganisationCollectionException> duplicatedCollectionName() {
    return () -> new OrganisationCollectionException(OrganisationCollectionErrorCase.OCE_DUB_001,
        "collection name existed");
  }

  public static Supplier<OrganisationCollectionException> cannotChangeAffiliate() {
    return () -> new OrganisationCollectionException(OrganisationCollectionErrorCase.OCE_CCA_001,
        "Cannot change affiliate");
  }

  public static Supplier<OrganisationCollectionException> customerExtendLimit() {
    return () -> new OrganisationCollectionException(OrganisationCollectionErrorCase.OCE_CEL_001,
        "Customer extended limit");
  }

  /**
   * Opening days error cases enumeration definition.
   */
  public enum OrganisationCollectionErrorCase implements IBusinessCode {
    OCE_DUB_001("DUPLICATED_COLLECTION_NAME"),
    OCE_NFA_001("AFFILIATE_NOT_FOUND"),
    OCE_NFC_001("COLLECTION_NOT_FOUND"),
    OCE_CCA_001("CANNOT_CHANGE_AFFILIATE"),
    OCE_CEL_001("CUSTOMER_EXTENDED_LIMIT");

    private String key;

    OrganisationCollectionErrorCase(String key) {
      this.key = key;
    }

    @Override
    public String code() {
      return this.name();
    }

    @Override
    public String key() {
      return this.key;
    }

  }
}
