package com.sagag.services.rest.resource;

import com.sagag.eshop.service.dto.BasketHistoryDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.data.domain.Page;
import org.springframework.hateoas.ResourceSupport;

/**
 * REST Basket history resource class.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BasketHistoryResource extends ResourceSupport {

  private Page<BasketHistoryDto> basketHistories;

  private long totalItems;

}
