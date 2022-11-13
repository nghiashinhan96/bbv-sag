import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ShopInfoModalComponent } from './shop-info-modal.component';

xdescribe('ShopInfoModalComponent', () => {
  let component: ShopInfoModalComponent;
  let fixture: ComponentFixture<ShopInfoModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ShopInfoModalComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ShopInfoModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
