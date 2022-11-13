package com.sagag.eshop.service.api.impl.offer;

import com.sagag.eshop.repo.api.EshopUserRepository;
import com.sagag.eshop.repo.api.offer.OfferPersonRepository;
import com.sagag.eshop.repo.api.offer.ViewOfferPersonRepository;
import com.sagag.eshop.repo.criteria.offer.OfferPersonSearchCriteria;
import com.sagag.eshop.repo.entity.EshopUser;
import com.sagag.eshop.repo.entity.Language;
import com.sagag.eshop.repo.entity.offer.OfferAddress;
import com.sagag.eshop.repo.entity.offer.OfferPerson;
import com.sagag.eshop.service.api.impl.OfferPersonServiceImpl;
import com.sagag.eshop.service.dto.UserInfo;
import com.sagag.eshop.service.dto.offer.OfferPersonDto;
import com.sagag.eshop.service.dto.offer.ViewOfferPersonDto;
import com.sagag.services.common.annotation.EshopMockitoJUnitRunner;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * UT for offer person service.
 */
@EshopMockitoJUnitRunner
public class OfferPersonServiceImplTest {

  private static final long CREATED_USER_ID = 13L;

  private static final int ORG_ID = 1;

  private final PageRequest firstPageRequest = PageRequest.of(0, 10);

  @InjectMocks
  private OfferPersonServiceImpl offerPersonService;

  @Mock
  private OfferPersonRepository offerPersonRepo;

  @Mock
  private OfferPerson offerPerson;

  @Mock
  private EshopUserRepository eshopUserRepo;

  @Mock
  private ViewOfferPersonRepository viewOfferPersonRepo;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);

    when(offerPerson.getOfferAddress()).thenReturn(OfferAddress.builder().build());
    when(offerPersonRepo.save(Mockito.any(OfferPerson.class))).thenReturn(offerPerson);
    when(offerPersonRepo.save(Mockito.any(OfferPerson.class))).thenReturn(offerPerson);
  }

  @Test
  public void testCreateNewOfferPerson() {
    final OfferPersonDto offerPerson = createOfferPersonDto();

    final EshopUser eshopUser = new EshopUser();
    eshopUser.setId(offerPerson.getCurrentUserId());
    eshopUser.setLanguage(Language.builder().id(1).build());
    when(eshopUserRepo.findById(offerPerson.getCurrentUserId())).thenReturn(Optional.of(eshopUser));

    final OfferPersonDto createdOfferPerson = offerPersonService.createOfferPerson(offerPerson);

    Assert.assertNotNull(createdOfferPerson);
    Assert.assertNotNull(createdOfferPerson.getId());

    Mockito.verify(offerPersonRepo, Mockito.times(2)).save(Mockito.any(OfferPerson.class));
  }

  @Test(expected = NoSuchElementException.class)
  public void testCreateNewOfferPerson_With_CreatedUserId_IsNull() {
    final OfferPersonDto offerPerson = createOfferPersonDto();
    offerPerson.setCurrentUserId(null);

    final UserInfo userInfo = new UserInfo();
    userInfo.setOrganisationId(ORG_ID);
    offerPersonService.createOfferPerson(offerPerson);
  }

  @SuppressWarnings("unchecked")
  @Test(expected = NoSuchElementException.class)
  public void testGetOfferPersonByCriteria_NotFoundResult() {
    when(
        viewOfferPersonRepo.findAll(Mockito.any(Specification.class), Mockito.eq(firstPageRequest)))
            .thenReturn(null);
    final OfferPersonSearchCriteria criteria =
        OfferPersonSearchCriteria.builder().name("testFirstName").organisationId(3).build();

    final Page<ViewOfferPersonDto> offerPerson =
        offerPersonService.searchOfferPersons(criteria, firstPageRequest);

    Assert.assertNotNull(offerPerson);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetOfferPersonByCriteria_With_NullCriteria() {
    offerPersonService.searchOfferPersons(null, firstPageRequest);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetOfferPersonByCriteria_With_NullOrgId() {
    offerPersonService.searchOfferPersons(new OfferPersonSearchCriteria(), firstPageRequest);
  }

  private OfferPersonDto createOfferPersonDto() {
    final OfferPersonDto offerPerson = new OfferPersonDto();
    offerPerson.setCurrentUserId(CREATED_USER_ID);
    offerPerson.setOrganisationId(ORG_ID);
    offerPerson.setSalutation("GENERAL_SALUTATION_FEMALE");
    offerPerson.setCompanyName("companyName test");
    offerPerson.setFirstName("firstName test");
    offerPerson.setLastName("lastName test");
    offerPerson.setRoad("road test");
    offerPerson.setAdditionalAddress1("additionalAddress1 test");
    offerPerson.setAdditionalAddress2("additionalAddress2 test");
    offerPerson.setPoBox("poBox test");
    offerPerson.setPostCode("1234");
    offerPerson.setPhone("123456789");
    offerPerson.setFax("123456");
    offerPerson.setEmail("email@bbv.vn");
    return offerPerson;
  }

  @Test
  public void testGetOfferPersonDetails_NotFoundResult() {
    Assert.assertEquals(offerPersonService.getOfferPersonDetails(10L), Optional.empty());
  }

  @Test
  public void testGetOfferPersonDetails() {
    Assert.assertNotNull(offerPersonService.getOfferPersonDetails(4L));
  }
}
