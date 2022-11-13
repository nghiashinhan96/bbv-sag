import { ComponentFixture, TestBed, async } from '@angular/core/testing';

import { HttpClientTestingModule } from "@angular/common/http/testing";
import { ShoppingOrderConditionComponent } from './shopping-order-condition.component';
import { UserDetail } from 'src/app/core/models/user-detail.model';

xdescribe('ShoppingOrderConditionComponent', () => {
  let component: ShoppingOrderConditionComponent;
  let fixture: ComponentFixture<ShoppingOrderConditionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ShoppingOrderConditionComponent],
      imports: [
        HttpClientTestingModule
      ]
    })
      .overrideTemplate(ShoppingOrderConditionComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ShoppingOrderConditionComponent);
    component = fixture.componentInstance;
    component.userDetail = new UserDetail(null);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
