import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ArticleFreeTextSearchComponent } from './article-free-text-search.component';

xdescribe('ArticleFreeTextSearchComponent', () => {
  let component: ArticleFreeTextSearchComponent;
  let fixture: ComponentFixture<ArticleFreeTextSearchComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ArticleFreeTextSearchComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ArticleFreeTextSearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
