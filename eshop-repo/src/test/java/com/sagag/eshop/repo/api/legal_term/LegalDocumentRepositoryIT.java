package com.sagag.eshop.repo.api.legal_term;

import com.sagag.eshop.repo.api.EshopUserRepository;
import com.sagag.eshop.repo.app.RepoApplication;
import com.sagag.eshop.repo.entity.EshopUser;
import static com.sagag.eshop.repo.enums.DocTypeAssignedStatus.ACTIVE;
import com.sagag.services.common.annotation.EshopIntegrationTest;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {RepoApplication.class})
@EshopIntegrationTest
@Transactional
public class LegalDocumentRepositoryIT {

    @Autowired
    private LegalDocumentRepository repository;

    @Autowired
    private EshopUserRepository eshopUserRepository;

    @Test
    public void shouldSaveLegalDoctypeSuccessFully() {
        EshopUser user = eshopUserRepository.findUserByUserId(1).orElse(null);
        if (Objects.isNull(user)) return;
        List<Map<String, Object>> result =
          repository.findByCustomerIdAndLanguageAndStatus(user.getId(), "DE", ACTIVE.getValue());
        Assertions.assertThat(result).isNotNull();
    }

}