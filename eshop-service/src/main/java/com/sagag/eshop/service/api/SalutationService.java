package com.sagag.eshop.service.api;

import com.sagag.eshop.repo.entity.Salutation;
import com.sagag.services.domain.eshop.dto.SalutationDto;

import java.util.List;

public interface SalutationService {

  /**
   * Returns available {@link SalutationDto} for profile type.
   *
   * @return a list of salutations for profile.
   */
  List<SalutationDto> getProfileSalutations();

  /**
   * get one by id
   * @param id
   * @return
   */
  Salutation getById(int id);

  /**
   * Returns list salutation for offer person
   *
   * @return the list of salutations for offer person
   */
  List<String> getOfferSalutations();

  List<SalutationDto> getSalutationByCodes(String... codes);
}
