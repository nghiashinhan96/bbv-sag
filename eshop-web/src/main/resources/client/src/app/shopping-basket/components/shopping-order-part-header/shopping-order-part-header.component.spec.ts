import { ComponentFixture, TestBed, async } from '@angular/core/testing';

import { LibUserSetting } from 'sag-article-detail';
import { ShoppingOrderPartHeaderComponent } from './shopping-order-part-header.component';
import { TranslateModule } from '@ngx-translate/core';

xdescribe('ShoppingOrderPartHeaderComponent', () => {
  let component: ShoppingOrderPartHeaderComponent;
  let fixture: ComponentFixture<ShoppingOrderPartHeaderComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ShoppingOrderPartHeaderComponent],
      imports: [
        TranslateModule.forRoot()
      ]
    })
      .overrideTemplate(ShoppingOrderPartHeaderComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ShoppingOrderPartHeaderComponent);
    component = fixture.componentInstance;
    component.userSetting = new LibUserSetting();
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
