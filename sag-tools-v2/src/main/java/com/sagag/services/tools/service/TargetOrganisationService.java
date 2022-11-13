package com.sagag.services.tools.service;

import java.util.List;

public interface TargetOrganisationService {

  List<Integer> findOrganisationIdHasOfferPermission(List<String> customerNumberList);

}
