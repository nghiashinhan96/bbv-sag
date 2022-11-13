import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GoodReceiverComponent } from './good-receiver.component';

xdescribe('GoodReceiverComponent', () => {
  let component: GoodReceiverComponent;
  let fixture: ComponentFixture<GoodReceiverComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [GoodReceiverComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GoodReceiverComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
