package com.sagag.eshop.repo.api.legal_term;

import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.entity.legal_term.LegalDoctype;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {RepoApplication.class})
@EshopIntegrationTest
@Transactional
public class LegalDoctypeRepositoryIT {

    @Autowired
    private LegalDoctypeRepository repository;

    @Test
    public void shouldSaveLegalDoctypeSuccessFully() {
        LegalDoctype legalDoctype = new LegalDoctype();
        legalDoctype.setDoctypeRef(0);
        LegalDoctype savedDoctype = repository.save(legalDoctype);
        Assertions.assertThat(savedDoctype).isNotNull();
    }

}