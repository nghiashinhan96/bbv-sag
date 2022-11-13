import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SaleControlComponent } from './sale-control.component';

xdescribe('SaleControlComponent', () => {
  let component: SaleControlComponent;
  let fixture: ComponentFixture<SaleControlComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [SaleControlComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SaleControlComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
