import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ArticleAvailPopupAvailabilityComponent } from './article-avail-popup-availability.component';

xdescribe('ArticleAvailPopupAvailabilityComponent', () => {
  let component: ArticleAvailPopupAvailabilityComponent;
  let fixture: ComponentFixture<ArticleAvailPopupAvailabilityComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ArticleAvailPopupAvailabilityComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ArticleAvailPopupAvailabilityComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
