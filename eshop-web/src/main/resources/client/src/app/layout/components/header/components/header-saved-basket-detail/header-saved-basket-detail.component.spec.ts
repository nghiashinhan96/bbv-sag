import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HeaderSavedBasketDetailComponent } from './header-saved-basket-detail.component';

xdescribe('HeaderSavedBasketDetailComponent', () => {
  let component: HeaderSavedBasketDetailComponent;
  let fixture: ComponentFixture<HeaderSavedBasketDetailComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [HeaderSavedBasketDetailComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HeaderSavedBasketDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
