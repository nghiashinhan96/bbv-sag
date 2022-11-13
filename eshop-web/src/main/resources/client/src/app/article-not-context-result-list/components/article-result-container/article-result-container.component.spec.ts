import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ArticleResultContainerComponent } from './article-result-container.component';

xdescribe('ArticleResultContainerComponent', () => {
  let component: ArticleResultContainerComponent;
  let fixture: ComponentFixture<ArticleResultContainerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ArticleResultContainerComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ArticleResultContainerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
