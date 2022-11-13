package com.sagag.services.service.resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.hateoas.ResourceSupport;

import java.util.List;

/**
 * Feedback master data response class.
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(Include.NON_NULL)
public class FeedbackMasterDataResource<T> extends ResourceSupport {

  private List<String> topicCodes;
  private T userData;
}
