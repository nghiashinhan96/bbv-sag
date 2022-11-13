import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OpeningDayListComponent } from './opening-day-list.component';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { TranslateModule, TranslateLoader, TranslateStaticLoader } from 'ng2-translate';
import { HttpModule, Http } from '@angular/http';
import { NgxPaginationModule } from 'ngx-pagination';

xdescribe('OpeningDayListComponent', () => {
  let component: OpeningDayListComponent;
  let fixture: ComponentFixture<OpeningDayListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        TranslateModule.forRoot({
          provide: TranslateLoader, HttpModule,
          useFactory: (http: Http) => new TranslateStaticLoader(http, 'assets/i18n', '.json'),
          deps: [Http]
        }),
        NgxPaginationModule
      ],
      declarations: [ OpeningDayListComponent ],
      schemas: [ NO_ERRORS_SCHEMA ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OpeningDayListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
