import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ArticleReturnContainerComponent } from './article-return-container.component';

xdescribe('ArticleReturnContainerComponent', () => {
  let component: ArticleReturnContainerComponent;
  let fixture: ComponentFixture<ArticleReturnContainerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ArticleReturnContainerComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ArticleReturnContainerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
