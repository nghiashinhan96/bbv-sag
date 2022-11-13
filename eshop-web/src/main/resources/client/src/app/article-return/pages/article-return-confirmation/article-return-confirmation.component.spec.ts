import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ArticleReturnConfirmationComponent } from './article-return-confirmation.component';

xdescribe('ArticleReturnConfirmationComponent', () => {
  let component: ArticleReturnConfirmationComponent;
  let fixture: ComponentFixture<ArticleReturnConfirmationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ArticleReturnConfirmationComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ArticleReturnConfirmationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
