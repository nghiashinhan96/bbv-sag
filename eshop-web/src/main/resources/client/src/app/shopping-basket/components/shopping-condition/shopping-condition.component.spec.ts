import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ShoppingConditionComponent } from './shopping-condition.component';
import { TranslateModule } from '@ngx-translate/core';

xdescribe('ShoppingConditionComponent', () => {
  let component: ShoppingConditionComponent;
  let fixture: ComponentFixture<ShoppingConditionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ShoppingConditionComponent],
      imports: [
        TranslateModule.forRoot(),
        FormsModule,
        ReactiveFormsModule
      ]
    })
      .overrideTemplate(ShoppingConditionComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ShoppingConditionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
