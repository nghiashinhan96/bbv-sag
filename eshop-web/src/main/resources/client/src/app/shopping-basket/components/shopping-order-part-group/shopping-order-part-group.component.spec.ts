import { ComponentFixture, TestBed, async } from '@angular/core/testing';

import { ShoppingOrderPartGroupComponent } from './shopping-order-part-group.component';
import { TranslateModule } from '@ngx-translate/core';

xdescribe('ShoppingOrderPartGroupComponent', () => {
  let component: ShoppingOrderPartGroupComponent;
  let fixture: ComponentFixture<ShoppingOrderPartGroupComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ShoppingOrderPartGroupComponent],
      imports: [
        TranslateModule.forRoot()
      ]
    })
      .overrideTemplate(ShoppingOrderPartGroupComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ShoppingOrderPartGroupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
