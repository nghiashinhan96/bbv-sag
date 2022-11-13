import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CustomerMemoComponent } from './customer-memo.component';

xdescribe('CustomerMemoComponent', () => {
  let component: CustomerMemoComponent;
  let fixture: ComponentFixture<CustomerMemoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CustomerMemoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CustomerMemoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
