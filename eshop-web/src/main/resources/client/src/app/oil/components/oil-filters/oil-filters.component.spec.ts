import { CommonModule } from '@angular/common';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { NgSelectModule } from '@ng-select/ng-select';
import { TranslateModule } from '@ngx-translate/core';
import { TabsModule } from 'ngx-bootstrap/tabs';
import { ConnectCommonModule } from 'src/app/shared/connect-common/connect-common.module';
import { OilFilter } from '../../models/oil-filter.model';
import { OilModel } from '../../models/oil.model';

import { OilFiltersComponent } from './oil-filters.component';

xdescribe('OilFiltersComponent', () => {
  let component: OilFiltersComponent;
  let fixture: ComponentFixture<OilFiltersComponent>;

  const inputMockupData: OilModel = new OilModel();
  inputMockupData.totalElements = 100;
  const inputMockupModels: OilFilter = new OilFilter();
  const inputMockupLoading: boolean = false;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ OilFiltersComponent ],
      imports: [
        CommonModule,
        FormsModule,
        TranslateModule.forRoot(),
        TabsModule.forRoot(),
        NgSelectModule,
        ConnectCommonModule,
        RouterTestingModule,
      ],
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OilFiltersComponent);
    component = fixture.componentInstance;
    component.data = inputMockupData;
    component.models = inputMockupModels;
    component.loading = inputMockupLoading;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should show the spinner icon', () => {
    component.loading = true;
    fixture.detectChanges();
    const compiled: HTMLElement = fixture.debugElement.nativeElement;
    const btnElement = compiled.querySelector("button[data-automation='oil-button-search'] > i.fa-spinner")
    expect(btnElement).toBeTruthy();
  });

  it('should not show the spinner icon', () => {
    component.loading = false;
    fixture.detectChanges();
    const compiled: HTMLElement = fixture.debugElement.nativeElement;
    const btnElement = compiled.querySelector("button[data-automation='oil-button-search'] > i.fa-spinner")
    expect(btnElement).toBeFalsy();
  });

  it('should render search button contain the totalElements as a string value', () => {
    component.loading = false;
    fixture.detectChanges();
    const compiled: HTMLElement = fixture.debugElement.nativeElement;
    const btnElement = compiled.querySelector("button[data-automation='oil-button-search']")
    expect(btnElement.textContent).toContain(inputMockupData.totalElements.toString());
  });

  afterAll(() => {
    TestBed.resetTestingModule();
  });
});
