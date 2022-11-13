import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GoodReceiverModalComponent } from './good-receiver-modal.component';

xdescribe('GoodReceiverModalComponent', () => {
  let component: GoodReceiverModalComponent;
  let fixture: ComponentFixture<GoodReceiverModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [GoodReceiverModalComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GoodReceiverModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
