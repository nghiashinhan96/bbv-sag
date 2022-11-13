package com.sagag.services.article.api;

import com.sagag.services.domain.sag.external.CustomerBranch;

import java.util.List;

public interface BranchExternalService {

  List<CustomerBranch> getBranches(String companyName);

}
