import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ArticleDetailImageComponent } from './article-detail-image.component';

xdescribe('ArticleDetailImageComponent', () => {
  let component: ArticleDetailImageComponent;
  let fixture: ComponentFixture<ArticleDetailImageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ArticleDetailImageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ArticleDetailImageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
