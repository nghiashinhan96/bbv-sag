import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HeaderSavedBasketComponent } from './header-saved-basket.component';

xdescribe('HeaderSavedBasketComponent', () => {
  let component: HeaderSavedBasketComponent;
  let fixture: ComponentFixture<HeaderSavedBasketComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [HeaderSavedBasketComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HeaderSavedBasketComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
