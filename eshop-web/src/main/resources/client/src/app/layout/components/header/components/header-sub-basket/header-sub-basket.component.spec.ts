import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HeaderSubBasketComponent } from './header-sub-basket.component';

xdescribe('HeaderSubBasketComponent', () => {
  let component: HeaderSubBasketComponent;
  let fixture: ComponentFixture<HeaderSubBasketComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [HeaderSubBasketComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HeaderSubBasketComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
