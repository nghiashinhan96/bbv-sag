package com.sagag.services.rest.resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sagag.services.domain.sag.legal_term.LegalTermDto;
import lombok.*;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;

@Builder
@Getter
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LegalTermResource extends ResourceSupport {

    private List<LegalTermDto> legalTerms;

    private Boolean hasExpiredTerms;

}
