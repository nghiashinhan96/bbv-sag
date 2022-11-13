package com.sagag.services.article.api.request;

import lombok.Data;

import java.io.Serializable;
import java.util.Collection;
import java.util.Optional;

/**
 * Class to contain basket position request to send to ERP.
 *
 */
@Data
public class BasketPositionRequest implements Serializable {

  private static final long serialVersionUID = 7409034874655516991L;

  private final Collection<ArticleRequest> articles; // required
  private final Optional<VehicleRequest> vehicle; // optional

}
