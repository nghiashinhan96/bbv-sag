package com.sagag.services.tools.batch.customer.klaus;

import com.sagag.services.tools.domain.customer.PermissionConfigurationDto;
import com.sagag.services.tools.domain.target.CollectionPermission;
import com.sagag.services.tools.domain.target.CollectionRelation;
import com.sagag.services.tools.domain.target.EshopPermission;
import com.sagag.services.tools.support.PermissionEnum;
import com.sagag.services.tools.support.SupportedAffiliate;

import java.util.List;
import java.util.function.Function;

public interface IKlausCustomerMapper {

  String DF_ORG_COLLECTION_NAME = "sag";

  String DUMMY_ORG_CODE = "800000";

  String COUNTRY_CODE = "CHE";

  default Function<EshopPermission, PermissionConfigurationDto> toPermissionDto(String affShortName,
      List<CollectionPermission> collectionPermissions, boolean isCustomerUser) {
    return perm -> PermissionConfigurationDto.builder().permission(perm.getPermission())
        .editable(isCustomerUser ?
            isAllow(affShortName, perm, collectionPermissions) :
            isPermBelongToAff(affShortName, perm.getPermission()))
        .langKey(perm.getPermissisonKey()).permissionId(perm.getId())
        .enable(isAllow(affShortName, perm, collectionPermissions)).build();
  }

  default boolean isAllow(String affShortName, EshopPermission permission,
      List<CollectionPermission> collectionPermissions) {
    return isPermBelongToAff(affShortName, permission.getPermission())
        && collectionPermissions.stream().map(CollectionPermission::getEshopPermissionId)
            .anyMatch(id -> permission.getId() == id);
  }

  static boolean isPermBelongToAff(String affShortName, String permName) {
    return !(isPermNotBelongToMatikCh(affShortName, permName)
        || isPermNotBelongToRbe(affShortName, permName)
        || isPermNotBelongToDdch(affShortName, permName)
        || isPermNotBelongToTnm(affShortName, permName));
  }

  static boolean isPermNotBelongToDdch(String affShortName, String permName) {
    return SupportedAffiliate.fromDesc(affShortName).get().isDch()
        && PermissionEnum.valueOf(permName).isWholesaler();
  }

  static boolean isPermNotBelongToTnm(String affShortName, String permName) {
    return SupportedAffiliate.fromDesc(affShortName).get().isTnm()
        && PermissionEnum.valueOf(permName).isWholesaler();
  }

  static boolean isPermNotBelongToMatikCh(String affShortName, String permName) {
    return SupportedAffiliate.fromDesc(affShortName).get().isMatikCh()
        && (PermissionEnum.valueOf(permName).isOil()
            || PermissionEnum.valueOf(permName).isWholesaler());
  }

  static boolean isPermNotBelongToRbe(String affShortName, String permName) {
    return SupportedAffiliate.fromDesc(affShortName).get().isRbe()
        && (PermissionEnum.valueOf(permName).isOffer()
            || PermissionEnum.valueOf(permName).isWholesaler());
  }

  default CollectionRelation buildOrgCollectionRelation(int orgCollectionId, int customerOrgId) {
    return CollectionRelation.builder()
        .collectionId(orgCollectionId)
        .organisationId(customerOrgId)
        .isActive(true)
        .build();
  }
}
