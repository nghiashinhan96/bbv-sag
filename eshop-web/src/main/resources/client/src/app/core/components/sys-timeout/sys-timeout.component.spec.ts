import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SysTimeoutComponent } from './sys-timeout.component';

xdescribe('SysTimeoutComponent', () => {
  let component: SysTimeoutComponent;
  let fixture: ComponentFixture<SysTimeoutComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [SysTimeoutComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SysTimeoutComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
