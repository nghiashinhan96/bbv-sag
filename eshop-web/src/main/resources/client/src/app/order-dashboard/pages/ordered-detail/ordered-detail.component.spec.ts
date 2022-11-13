import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OrderedDetailComponent } from './ordered-detail.component';

xdescribe('OrderedDetailComponent', () => {
  let component: OrderedDetailComponent;
  let fixture: ComponentFixture<OrderedDetailComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [OrderedDetailComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OrderedDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
