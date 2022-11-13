import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ArticleDetailSpecVehicleUsageComponent } from './article-detail-spec-vehicle-usage.component';

xdescribe('ArticleDetailSpecVehicleUsageComponent', () => {
  let component: ArticleDetailSpecVehicleUsageComponent;
  let fixture: ComponentFixture<ArticleDetailSpecVehicleUsageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ArticleDetailSpecVehicleUsageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ArticleDetailSpecVehicleUsageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
