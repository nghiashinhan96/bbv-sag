import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ArticleDetailSpecComponent } from './article-detail-spec.component';

xdescribe('ArticleDetailSpecComponent', () => {
  let component: ArticleDetailSpecComponent;
  let fixture: ComponentFixture<ArticleDetailSpecComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ArticleDetailSpecComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ArticleDetailSpecComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
