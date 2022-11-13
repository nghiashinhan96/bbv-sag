package com.sagag.services.rest.resource;

import io.swagger.annotations.ApiModelProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.HttpStatus;

@Data
@EqualsAndHashCode(callSuper = false)
public class DefaultResource extends ResourceSupport {

  @ApiModelProperty(name = "The http status of response", allowableValues = "200, 201, 400, 404")
  private HttpStatus status;

  private String message;

  public DefaultResource() {
    this.status = HttpStatus.OK;
    this.message = "successful";
  }

  public static DefaultResource ok(String message) {
    DefaultResource resource = new DefaultResource();
    resource.setStatus(HttpStatus.OK);
    resource.setMessage(message);
    return resource;
  }
}
