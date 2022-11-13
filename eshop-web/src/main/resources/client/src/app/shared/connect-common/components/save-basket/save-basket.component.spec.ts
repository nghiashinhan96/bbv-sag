import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SaveBasketComponent } from './save-basket.component';

xdescribe('SaveBasketComponent', () => {
  let component: SaveBasketComponent;
  let fixture: ComponentFixture<SaveBasketComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [SaveBasketComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SaveBasketComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
