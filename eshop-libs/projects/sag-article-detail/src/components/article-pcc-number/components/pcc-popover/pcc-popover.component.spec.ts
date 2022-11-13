import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PccPopoverComponent } from './pcc-popover.component';

xdescribe('PccPopoverComponent', () => {
  let component: PccPopoverComponent;
  let fixture: ComponentFixture<PccPopoverComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PccPopoverComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PccPopoverComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
