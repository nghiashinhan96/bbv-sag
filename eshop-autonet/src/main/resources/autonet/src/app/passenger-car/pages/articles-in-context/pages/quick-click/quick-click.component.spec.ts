import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { QuickClickComponent } from './quick-click.component';

xdescribe('QuickClickComponent', () => {
  let component: QuickClickComponent;
  let fixture: ComponentFixture<QuickClickComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ QuickClickComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(QuickClickComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
