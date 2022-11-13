package com.sagag.services.domain.unitree;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sagag.services.domain.eshop.unitree.dto.UnitreeFreetextSearchDto;

import lombok.Data;

import org.springframework.data.domain.Page;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FreetextUnitreeResponse implements Serializable {

  private static final long serialVersionUID = -9198273579598938803L;

  private Page<UnitreeFreetextSearchDto> unitrees;
}