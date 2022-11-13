import { ComponentFixture, TestBed, async } from '@angular/core/testing';

import { LibUserSetting } from 'sag-article-detail';
import { ShoppingOrderPartListComponent } from './shopping-order-part-list.component';

xdescribe('ShoppingOrderPartListComponent', () => {
  let component: ShoppingOrderPartListComponent;
  let fixture: ComponentFixture<ShoppingOrderPartListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ShoppingOrderPartListComponent]
    })
      .overrideTemplate(ShoppingOrderPartListComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ShoppingOrderPartListComponent);
    component = fixture.componentInstance;
    component.userSetting = new LibUserSetting();
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
