import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CustomerGroupSearchFormComponent } from './customer-group-search-form.component';

xdescribe('CustomerGroupSearchFormComponent', () => {
  let component: CustomerGroupSearchFormComponent;
  let fixture: ComponentFixture<CustomerGroupSearchFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CustomerGroupSearchFormComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CustomerGroupSearchFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
