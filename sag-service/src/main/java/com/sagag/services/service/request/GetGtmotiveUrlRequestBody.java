package com.sagag.services.service.request;


import com.sagag.services.gtmotive.domain.request.GtmotiveCriteria;
import lombok.Data;

import java.io.Serializable;

/**
 * Request body class for getting Gtmotive URL embedded into iframe.
 */
@Data
public class GetGtmotiveUrlRequestBody implements Serializable {

  private static final long serialVersionUID = -6381977439914251196L;

  private GtmotiveCriteria criteria;
}
