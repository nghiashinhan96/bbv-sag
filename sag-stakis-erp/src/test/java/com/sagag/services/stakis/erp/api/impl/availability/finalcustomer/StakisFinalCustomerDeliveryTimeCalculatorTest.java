package com.sagag.services.stakis.erp.api.impl.availability.finalcustomer;

import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import com.sagag.services.article.api.availability.finalcustomer.FinalCustomerAvailabilityFilter;
import com.sagag.services.article.api.availability.finalcustomer.impl.FinalCustomerPickupAvailabilityFilterImpl;
import com.sagag.services.article.api.availability.finalcustomer.impl.FinalCustomerTourAvailabilityFilterImpl;
import com.sagag.services.common.enums.ErpSendMethodEnum;
import com.sagag.services.common.utils.SagJSONUtil;
import com.sagag.services.domain.article.ArticleDocDto;
import com.sagag.services.domain.eshop.dto.WssDeliveryProfileDto;
import com.sagag.services.domain.sag.erp.Availability;
import com.sagag.services.stakis.erp.availability.CzArticleAvailabilityProcessor;
import com.sagag.services.stakis.erp.availability.finalcustomer.impl.StakisFinalCustomerDeliveryTimeCalculatorImpl;
import com.sagag.services.stakis.erp.enums.StakisArticleAvailabilityState;

@RunWith(MockitoJUnitRunner.class)
public class StakisFinalCustomerDeliveryTimeCalculatorTest {

	private static final int MAX_AVAILABILITY_DAY_RANGE = 30;

	@InjectMocks
	private StakisFinalCustomerDeliveryTimeCalculatorImpl finalCustomerPriceCalculator;

	@Mock
	private FinalCustomerPickupAvailabilityFilterImpl finalCustomerPickupAvailabilityFilter;

	@Mock
	private FinalCustomerTourAvailabilityFilterImpl finalCustomerTourAvailabilityFilter;

	@Mock
	private CzArticleAvailabilityProcessor availabilityProcessor;

	@Spy
	private List<FinalCustomerAvailabilityFilter> finalCustomerAvailabilityFilters;

	private ArticleDocDto articleDocDto;

	private WssDeliveryProfileDto wssDeliveryProfileDto;

	private Availability finalCustomerTourAvail;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		articleDocDto = initArticleDocDto();
		wssDeliveryProfileDto = initWssDeliveryProfileDto();
		finalCustomerTourAvail = initFinalCustomerTourAvailability();
		when(availabilityProcessor.getDefaultResultForNoPrice())
				.thenReturn(StakisArticleAvailabilityState.BLACK.toResult());
		when(finalCustomerPickupAvailabilityFilter.sendMethod()).thenReturn(ErpSendMethodEnum.PICKUP);
		when(finalCustomerTourAvailabilityFilter.sendMethod()).thenReturn(ErpSendMethodEnum.TOUR);
		when(finalCustomerAvailabilityFilters.stream())
				.thenReturn(Stream.of(finalCustomerPickupAvailabilityFilter, finalCustomerTourAvailabilityFilter));

	}

	@Test
	public void testCalculateAvailabilitiesForFinalCustomer_noDeliveryProfileShouldReturnDefault() throws IOException {
		ArticleDocDto articleDocDto = initArticleDocDto();

		final List<Availability> finalCustomerAvailabilities = finalCustomerPriceCalculator
				.calculateAvailabilitiesForFinalCustomer(articleDocDto, null, null, null);
		Assert.assertTrue(CollectionUtils.isNotEmpty(finalCustomerAvailabilities));
		Assert.assertTrue(finalCustomerAvailabilities.size() == 1);
		Availability latestAvailability = articleDocDto.getLastestAvailability();
		final String articleLatestArrivalTime = latestAvailability.getArrivalTime();
		final String arrivalTimeForFinalCustomer = finalCustomerAvailabilities.get(0).getArrivalTime();
		Assert.assertTrue(StringUtils.equalsIgnoreCase(articleLatestArrivalTime, arrivalTimeForFinalCustomer));
	}

	@Test
	public void testCalculateAvailabilitiesForFinalCustomer_notCalculableShouldReturnDefault() throws IOException {
		final Availability lastestAvailability = articleDocDto.getLastestAvailability();
		when(finalCustomerPickupAvailabilityFilter.filterAvailability(lastestAvailability, wssDeliveryProfileDto,
				MAX_AVAILABILITY_DAY_RANGE)).thenReturn(null);

		final List<Availability> finalCustomerAvailabilities = finalCustomerPriceCalculator
				.calculateAvailabilitiesForFinalCustomer(articleDocDto, ErpSendMethodEnum.PICKUP.name(),
						wssDeliveryProfileDto, MAX_AVAILABILITY_DAY_RANGE);
		Assert.assertTrue(CollectionUtils.isNotEmpty(finalCustomerAvailabilities));
		Assert.assertTrue(finalCustomerAvailabilities.size() == 1);
		final String articleLatestArrivalTime = lastestAvailability.getArrivalTime();
		final String arrivalTimeForFinalCustomer = finalCustomerAvailabilities.get(0).getArrivalTime();
		Assert.assertTrue(StringUtils.equalsIgnoreCase(articleLatestArrivalTime, arrivalTimeForFinalCustomer));
	}

	@Test
	public void testCalculateAvailabilitiesForFinalCustomer_tourShouldReturnAvailability() throws IOException {
		final Availability lastestAvailability = articleDocDto.getLastestAvailability();
		when(finalCustomerTourAvailabilityFilter.filterAvailability(lastestAvailability, wssDeliveryProfileDto,
				MAX_AVAILABILITY_DAY_RANGE)).thenReturn(finalCustomerTourAvail);

		final List<Availability> finalCustomerAvailabilities = finalCustomerPriceCalculator
				.calculateAvailabilitiesForFinalCustomer(articleDocDto, ErpSendMethodEnum.TOUR.name(),
						wssDeliveryProfileDto, MAX_AVAILABILITY_DAY_RANGE);
		Assert.assertTrue(CollectionUtils.isNotEmpty(finalCustomerAvailabilities));
		Assert.assertTrue(finalCustomerAvailabilities.size() == 1);
		final String articleLatestArrivalTime = finalCustomerTourAvail.getArrivalTime();
		final String arrivalTimeForFinalCustomer = finalCustomerAvailabilities.get(0).getArrivalTime();
		Assert.assertTrue(StringUtils.equalsIgnoreCase(articleLatestArrivalTime, arrivalTimeForFinalCustomer));
	}

	@Test
	public void testCalculateAvailabilitiesForFinalCustomer_noWholesalerAvailShouldReturnDefault() throws IOException {
		articleDocDto.getAvailabilities().forEach(avail -> avail.setBackOrder(true));
		final Availability lastestAvailability = articleDocDto.getLastestAvailability();

		final List<Availability> finalCustomerAvailabilities = finalCustomerPriceCalculator
				.calculateAvailabilitiesForFinalCustomer(articleDocDto, ErpSendMethodEnum.PICKUP.name(),
						wssDeliveryProfileDto, MAX_AVAILABILITY_DAY_RANGE);
		Assert.assertTrue(CollectionUtils.isNotEmpty(finalCustomerAvailabilities));
		Assert.assertTrue(finalCustomerAvailabilities.size() == 1);
		final String articleLatestArrivalTime = lastestAvailability.getArrivalTime();
		final String arrivalTimeForFinalCustomer = finalCustomerAvailabilities.get(0).getArrivalTime();
		final int finalCustomerAvailState = finalCustomerAvailabilities.get(0).getAvailState();
		Assert.assertTrue(StringUtils.equalsIgnoreCase(articleLatestArrivalTime, arrivalTimeForFinalCustomer));
		Assert.assertTrue(finalCustomerAvailState == StakisArticleAvailabilityState.BLACK.getCode());
	}

	@Test
	public void testCalculateAvailabilitiesForFinalCustomer_latestArrivalTimeBLankShouldReturnDefault()
			throws IOException {
		articleDocDto.getAvailabilities().forEach(avail -> avail.setBackOrder(true));
		final Availability lastestAvailability = articleDocDto.getLastestAvailability();
		lastestAvailability.setRawArrivalTime(StringUtils.EMPTY);

		final List<Availability> finalCustomerAvailabilities = finalCustomerPriceCalculator
				.calculateAvailabilitiesForFinalCustomer(articleDocDto, ErpSendMethodEnum.PICKUP.name(),
						wssDeliveryProfileDto, MAX_AVAILABILITY_DAY_RANGE);
		Assert.assertTrue(CollectionUtils.isNotEmpty(finalCustomerAvailabilities));
		Assert.assertTrue(finalCustomerAvailabilities.size() == 1);
		final String articleLatestArrivalTime = lastestAvailability.getArrivalTime();
		final String arrivalTimeForFinalCustomer = finalCustomerAvailabilities.get(0).getArrivalTime();
		final int finalCustomerAvailState = finalCustomerAvailabilities.get(0).getAvailState();
		Assert.assertTrue(StringUtils.equalsIgnoreCase(articleLatestArrivalTime, arrivalTimeForFinalCustomer));
		Assert.assertTrue(finalCustomerAvailState == StakisArticleAvailabilityState.BLACK.getCode());
	}

	@Test
	public void testCalculateAvailabilitiesForFinalCustomer_branchNoOpeningShouldReturnDefault() throws IOException {
		articleDocDto.getAvailabilities().forEach(avail -> avail.setBackOrder(true));
		wssDeliveryProfileDto.getWssBranch().setWssBranchOpeningTimes(Lists.emptyList());
		final List<Availability> finalCustomerAvailabilities = finalCustomerPriceCalculator
				.calculateAvailabilitiesForFinalCustomer(articleDocDto, ErpSendMethodEnum.PICKUP.name(),
						wssDeliveryProfileDto, MAX_AVAILABILITY_DAY_RANGE);
		Assert.assertTrue(CollectionUtils.isNotEmpty(finalCustomerAvailabilities));
		Assert.assertTrue(finalCustomerAvailabilities.size() == 1);
		final String arrivalTimeForFinalCustomer = finalCustomerAvailabilities.get(0).getArrivalTime();
		final int finalCustomerAvailState = finalCustomerAvailabilities.get(0).getAvailState();
		Assert.assertTrue(StringUtils.isBlank(arrivalTimeForFinalCustomer));
		Assert.assertTrue(finalCustomerAvailState == StakisArticleAvailabilityState.BLACK.getCode());
	}

	private ArticleDocDto initArticleDocDto() throws IOException {
		final File file = new File(FilenameUtils.separatorsToSystem("src/test/resources/articles/10154695.json"));
		final ArticleDocDto article = SagJSONUtil.convertJsonToObject(FileUtils.readFileToString(file, "UTF-8"),
				ArticleDocDto.class);
		return article;
	}

	private WssDeliveryProfileDto initWssDeliveryProfileDto() throws IOException {
		final File file = new File(FilenameUtils
				.separatorsToSystem("src/test/resources/deliveryprofile/finalcustomer_deliveryprofile.json"));
		final WssDeliveryProfileDto deliveryProfile = SagJSONUtil
				.convertJsonToObject(FileUtils.readFileToString(file, "UTF-8"), WssDeliveryProfileDto.class);
		return deliveryProfile;
	}

	private Availability initFinalCustomerTourAvailability() throws IOException {
		final File file = new File(
				FilenameUtils.separatorsToSystem("src/test/resources/availability/finalcustomer/tour.json"));
		final Availability defaultFinalCustomerTourAvail = SagJSONUtil
				.convertJsonToObject(FileUtils.readFileToString(file, "UTF-8"), Availability.class);
		return defaultFinalCustomerTourAvail;
	}

}
