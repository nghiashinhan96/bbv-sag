import { CommonModule } from '@angular/common';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { NgSelectModule } from '@ng-select/ng-select';
import { TranslateModule } from '@ngx-translate/core';
import { TabsModule } from 'ngx-bootstrap/tabs';
import { ConnectCommonModule } from 'src/app/shared/connect-common/connect-common.module';
import { BulbFilter } from '../../models/bulb-filter.model';
import { BulbsModel } from '../../models/bulbs.model';

import { BulbFiltersComponent } from './bulb-filters.component';

xdescribe('BulbFiltersComponent', () => {
  let component: BulbFiltersComponent;
  let fixture: ComponentFixture<BulbFiltersComponent>;

  const inputMockupData: BulbsModel = new BulbsModel();
  inputMockupData.totalElements = 100;
  const inputMockupModels: BulbFilter = new BulbFilter();
  const inputMockupLoading: boolean = false;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BulbFiltersComponent ],
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
    fixture = TestBed.createComponent(BulbFiltersComponent);
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
    const btnElement = compiled.querySelector("button[data-automation='bulb-button-search'] > i.fa-spinner")
    expect(btnElement).toBeTruthy();
  });

  it('should not show the spinner icon', () => {
    component.loading = false;
    fixture.detectChanges();
    const compiled: HTMLElement = fixture.debugElement.nativeElement;
    const btnElement = compiled.querySelector("button[data-automation='bulb-button-search'] > i.fa-spinner")
    expect(btnElement).toBeFalsy();
  });

  it('should render search button contain the totalElements as a string value', () => {
    component.loading = false;
    fixture.detectChanges();
    const compiled: HTMLElement = fixture.debugElement.nativeElement;
    const btnElement = compiled.querySelector("button[data-automation='bulb-button-search']")
    expect(btnElement.textContent).toContain(inputMockupData.totalElements.toString());
  });

  afterAll(() => {
    TestBed.resetTestingModule();
  });
});
