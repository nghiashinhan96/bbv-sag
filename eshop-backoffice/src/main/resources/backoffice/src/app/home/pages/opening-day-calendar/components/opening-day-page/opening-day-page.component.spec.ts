import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OpeningDayPageComponent } from './opening-day-page.component';
import { RouterModule } from '@angular/router';
import { CommonModule, APP_BASE_HREF } from '@angular/common';
import { SharedModule } from 'app/shared/shared.module';
import { TranslateModule, TranslateLoader, TranslateStaticLoader, TranslateService } from 'ng2-translate';
import { Http, HttpModule } from '@angular/http';
import { ApiService } from 'app/core/services/api.service';
import { BrowserService } from 'app/core/services/browser.service';
import { provideAuth } from 'angular2-jwt';
import { Component, NO_ERRORS_SCHEMA } from '@angular/core';
import { OpeningDayCalendarService } from '../../service/opening-day-calendar.service';
import { FilterDateService } from '../../service/filter-date.service';

@Component({
  selector: 'backoffice-opening-day-date-filter',
  template: `<span>Date Filter Mock component</span>`
})
export class OpeningDayDateFilterMockComponent {}

@Component({
  selector: 'backoffice-opening-day-list',
  template: `<span>List Mock component</span>`
})
export class OpeningDayListMockComponent {}

xdescribe('OpeningDayPageComponent', () => {
  let component: OpeningDayPageComponent;
  let fixture: ComponentFixture<OpeningDayPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      schemas: [ NO_ERRORS_SCHEMA ],
      imports: [
        RouterModule.forRoot([]), CommonModule, SharedModule,
        TranslateModule.forRoot({
          provide: TranslateLoader, HttpModule,
          useFactory: (http: Http) => new TranslateStaticLoader(http, 'assets/i18n', '.json'),
          deps: [Http]
        })
      ],
      declarations: [
        OpeningDayPageComponent,
        OpeningDayDateFilterMockComponent,
        OpeningDayListMockComponent
      ],
      providers: [
        TranslateService, ApiService, BrowserService, OpeningDayCalendarService, FilterDateService,
        provideAuth({
          headerName: 'Authorization',
          headerPrefix: 'bearer',
          tokenName: 'token',
          tokenGetter: (() => localStorage.getItem('id_token')),
          globalHeaders: [{ 'Content-Type': 'application/json' }],
          noJwtError: true
      }), { provide: APP_BASE_HREF, useValue: '/' }
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OpeningDayPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
