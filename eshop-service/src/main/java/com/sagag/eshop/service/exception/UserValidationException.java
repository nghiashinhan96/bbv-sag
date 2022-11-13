package com.sagag.eshop.service.exception;

import com.sagag.services.common.exception.IBusinessCode;
import com.sagag.services.common.exception.ValidationException;

import lombok.Getter;

/**
 * Generic exception class for user validation.
 */
@Getter
public class UserValidationException extends ValidationException {

  private static final long serialVersionUID = -3123857820574377528L;

  public UserValidationException(UserErrorCase error, String msg) {
    super(msg);
    setCode(error.code());
    setKey(error.key());
  }

  /**
   * User error cases enumeration definition.
   */
  public enum UserErrorCase implements IBusinessCode {

    UE_NFU_001("NOT_FOUND_USER"), // user not found
    UE_WPF_001("WRONG_FORMAT_PASSWORD"), //
    UE_WOP_001("WRONG_OLD_PASSWORD"), //
    UE_IAF_001("INVALID_AFFILIATE"), //
    UE_ICN_001("INVALID_COMPANY_NAME"), //
    UE_IOT_001("INVALID_ORGANISATION_TYPE"), //
    UE_IEO_001("INVALID_EXTERNAL_ORGANISATION"), //
    UE_ADR_001("INVALID_ADMIN_ROLE"), //
    UE_IUR_001("INVALID_USER_ROLE"), //
    UE_IPC_001("INVALID_POST_CODE"), //
    UE_IUN_001("INVALID_USER_NAME"), //
    UE_ISN_001("INVALID_SUR_NAME"), //
    UE_IEM_001("INVALID_EMAIL"), //
    UE_ICU_001("INVALID_USERNAME_IN_CUSTOMER"), //
    UE_ICR_001("INVALID_CUSTOMER_ALREADY_REGISTERED"), //
    UE_IAU_001("INVALID_USERNAME_IN_AFFILIATE"), //
    UE_ISL_001("INVALID_SALUTATION"), //
    UE_IDM_001("INVALID_DELIVERY_METHOD"), //
    UE_IGR_001("INVALID_GROUP"), //
    UE_ICT_001("INVALID_CUSTOMER"), //
    UE_IUP_001("INVALID_USER_PROFILE"), //
    UE_CIN_001("CUSTOMER_INACTIVE"), //
    UE_NFE_001("NOT_FOUND_EMAIL_ADDRESS"), //
    UE_MEA_001("MULTIPLE_MAIL_ADDRESSES"), //
    UE_NAU_001("NOT_AUTHORIZED_USER"), //
    UE_MCC_001("CREATE_MDM_CUSTOMER_FAILED"), //
    UE_MUC_001("CREATE_MDM_USER_FAILED"), //
    UE_UBN_001("NOT_FOUND_USER_ON_BEHALF"), //
    UE_DUA_001("DUPLICATED_USERNAME_IN_AFFILIATE"), //
    UE_CBA_001("CUSTOMER_NOT_BELONG_TO_AFFILIATE"), //
    UE_EON_001("NOT_FOUND_EXTERNAL_ORGANISATION"), //
    UE_NFO_001("NOT_FOUND_ORGANISATION"), //
    UE_SMF_001("SEND_MAIL_FAILED"), //
    UE_ICO_001("INVALID_COLLECTION"); //

    private String key;

    UserErrorCase(String key) {
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