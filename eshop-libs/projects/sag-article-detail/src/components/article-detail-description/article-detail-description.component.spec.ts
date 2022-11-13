import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ArticleDetailDescriptionComponent } from './article-detail-description.component';

xdescribe('ArticleDetailDescriptionComponent', () => {
  let component: ArticleDetailDescriptionComponent;
  let fixture: ComponentFixture<ArticleDetailDescriptionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ArticleDetailDescriptionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ArticleDetailDescriptionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
