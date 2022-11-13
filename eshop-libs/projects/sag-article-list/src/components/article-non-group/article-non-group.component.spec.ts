import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ArticleNonGroupComponent } from './article-non-group.component';

xdescribe('ArticleNonGroupComponent', () => {
  let component: ArticleNonGroupComponent;
  let fixture: ComponentFixture<ArticleNonGroupComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ArticleNonGroupComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ArticleNonGroupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
