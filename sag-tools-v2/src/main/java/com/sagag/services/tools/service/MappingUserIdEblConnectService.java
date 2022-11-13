package com.sagag.services.tools.service;

import java.util.List;
import java.util.Optional;

public interface MappingUserIdEblConnectService {

  Long searchUserIdByEbl(Long eblUserId);

  Optional<Long> searchEblOrgId(String customerNr);

  List<Long> searchVendorIds(List<String> custNrs);
}
