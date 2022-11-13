package com.sagag.services.article.api;

import com.sagag.services.domain.sag.external.Courier;

import java.util.List;

public interface CourierExternalService {

  List<Courier> getCouriers(String companyName);

}
