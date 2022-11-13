import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OpeningDayDateFilterComponent } from './opening-day-date-filter.component';
import { FilterDateService } from '../../service/filter-date.service';
import { TranslateModule, TranslateLoader, TranslateStaticLoader } from 'ng2-translate';
import { HttpModule, Http } from '@angular/http';
import { NO_ERRORS_SCHEMA } from '@angular/core';

xdescribe('OpeningDayDateFilterComponent', () => {
    let component: OpeningDayDateFilterComponent;
    let fixture: ComponentFixture<OpeningDayDateFilterComponent>;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [
          TranslateModule.forRoot({
            provide: TranslateLoader, HttpModule,
            useFactory: (http: Http) => new TranslateStaticLoader(http, 'assets/i18n', '.json'),
            deps: [Http]
          })
        ],
        declarations: [ OpeningDayDateFilterComponent ],
        providers: [ FilterDateService ],
        schemas: [ NO_ERRORS_SCHEMA ]
      })
      .compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(OpeningDayDateFilterComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });

    it('should initiate values', () => {
        expect(component.isFilterDateOptionVisible).toBeTruthy();
        expect(component.filterRangeOptions.length).toEqual(4);

        fixture.whenStable().then(() => {
            expect(component.dateRangePickerSetting.alignSelectorRight).toBeTruthy();
            expect(component.selectedFilterDate.value).toEqual('0');
            expect(component.selectedFilterDate.label).toEqual('OPENING_DAY.FILTER_DATE.TODAY');
        });
    });

    it('should validate date range change', () => {
        expect(component.isNotValidDateRange).toBeFalsy();

        component.filterDateFromModel.date = { year: 2020, month: 1, day: 1};
        component.validateDateRange();
        expect(component.isNotValidDateRange).toBeTruthy();
    });

    it('should set date filter with provided date', () => {
        component.onDateRangeChange({ date: { year: 2019, month: 1, day: 1},
          jsdate:  new Date(), formatted: '', epoc: 0});
        expect(component.isDateRangeSearchActivated).toBeFalsy();
        expect(component.isFilterDateOptionVisible).toBeFalsy();
        expect(component.isPredefinedDateRangeSearchActivated).toBeTruthy();
        expect(component.selectedFilterDateIndex).toBeNull();
    });

    it('should set filter text', () => {
        component.onFilterSelected({ value: '4', label: '31 days' });

        expect(component.isDateRangeSearchActivated).toBeFalsy();
        expect(component.isFilterDateOptionVisible).toBeTruthy();
        expect(component.isPredefinedDateRangeSearchActivated).toBeTruthy();
  });
});
