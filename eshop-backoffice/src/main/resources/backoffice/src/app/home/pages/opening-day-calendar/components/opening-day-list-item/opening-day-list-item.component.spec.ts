import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OpeningDayListItemComponent } from './opening-day-list-item.component';
import { TranslateModule, TranslateLoader, TranslateStaticLoader } from 'ng2-translate';
import { HttpModule, Http } from '@angular/http';
import { NO_ERRORS_SCHEMA } from '@angular/core';

xdescribe('OpeningDayListItemComponent', () => {
  let component: OpeningDayListItemComponent;
  let fixture: ComponentFixture<OpeningDayListItemComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        TranslateModule.forRoot({
          provide: TranslateLoader, HttpModule,
          useFactory: (http: Http) => new TranslateStaticLoader(http, 'assets/i18n', '.json'),
          deps: [Http]
        })
      ],
      declarations: [ OpeningDayListItemComponent ],
      schemas: [ NO_ERRORS_SCHEMA ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OpeningDayListItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
