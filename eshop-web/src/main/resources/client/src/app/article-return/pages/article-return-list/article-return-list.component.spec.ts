import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ArticleReturnListComponent } from './article-return-list.component';

xdescribe('ArticleReturnListComponent', () => {
  let component: ArticleReturnListComponent;
  let fixture: ComponentFixture<ArticleReturnListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ArticleReturnListComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ArticleReturnListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
