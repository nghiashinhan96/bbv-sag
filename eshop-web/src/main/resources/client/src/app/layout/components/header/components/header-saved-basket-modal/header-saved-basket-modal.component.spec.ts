import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HeaderSavedBasketModalComponent } from './header-saved-basket-modal.component';

xdescribe('HeaderSavedBasketModalComponent', () => {
  let component: HeaderSavedBasketModalComponent;
  let fixture: ComponentFixture<HeaderSavedBasketModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [HeaderSavedBasketModalComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HeaderSavedBasketModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
