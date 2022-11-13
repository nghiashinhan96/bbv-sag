import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SagLibArticleListComponent } from './article-list.component';

xdescribe('ArticleListComponent', () => {
  let component: SagLibArticleListComponent;
  let fixture: ComponentFixture<SagLibArticleListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SagLibArticleListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SagLibArticleListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
