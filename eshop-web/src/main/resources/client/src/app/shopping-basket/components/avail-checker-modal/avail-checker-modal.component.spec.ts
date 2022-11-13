import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AvailCheckerModalComponent } from './avail-checker-modal.component';

xdescribe('AvailCheckerModalComponent', () => {
  let component: AvailCheckerModalComponent;
  let fixture: ComponentFixture<AvailCheckerModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [AvailCheckerModalComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AvailCheckerModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
