import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AutonetFrameComponent } from './autonet-frame.component';

xdescribe('AutonetFrameComponent', () => {
  let component: AutonetFrameComponent;
  let fixture: ComponentFixture<AutonetFrameComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AutonetFrameComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AutonetFrameComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
