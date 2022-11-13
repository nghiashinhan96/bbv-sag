import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SaglibQuickClickComponent } from './quick-click.component';

xdescribe('QuickClickComponent', () => {
  let component: SaglibQuickClickComponent;
  let fixture: ComponentFixture<SaglibQuickClickComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SaglibQuickClickComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SaglibQuickClickComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
