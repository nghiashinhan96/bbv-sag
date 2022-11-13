import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CustomerSearchFormComponent } from './customer-search-form.component';

xdescribe('CustomerSearchFormComponent', () => {
  let component: CustomerSearchFormComponent;
  let fixture: ComponentFixture<CustomerSearchFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CustomerSearchFormComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CustomerSearchFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
