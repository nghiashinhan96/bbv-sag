package com.sagag.services.ax.availability;

import org.mockito.Mock;

import com.sagag.eshop.repo.api.TourTimeRepository;
import com.sagag.services.article.api.ArticleExternalService;
import com.sagag.services.ax.availability.calculator.PickupArrivalTimeCalculator;
import com.sagag.services.ax.availability.tourtime.impl.AxBackOrderTourTimeTableGenerator;
import com.sagag.services.ax.holidays.impl.AxDefaultHolidaysCheckerImpl;

public abstract class AbstractAxAvailabilityTest {

  @Mock
  protected AxDefaultHolidaysCheckerImpl axAustriaHolidaysChecker;

  @Mock
  protected TourTimeRepository tourTimeRepo;

  @Mock
  protected AxBackOrderTourTimeTableGenerator axTourTimeTableGenerator;

  @Mock
  protected ArticleExternalService axService;

  @Mock
  protected PickupArrivalTimeCalculator arrivalTimeCalculator;

}
