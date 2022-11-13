package com.sagag.services.oates.dto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OatesEquipmentProductsDto implements Serializable {

	private static final long serialVersionUID = 4063748227060356708L;

	private List<OatesApplicationDto> applications;

	private List<OatesDecisionTreeDto> decistionTree;

}
