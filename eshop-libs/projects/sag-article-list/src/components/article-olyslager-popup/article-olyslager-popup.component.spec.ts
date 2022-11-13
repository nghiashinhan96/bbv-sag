import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ArticleOlyslagerPopupComponent } from './article-olyslager-popup.component';

xdescribe('ArticleOlyslagerPopupComponent', () => {
  let component: ArticleOlyslagerPopupComponent;
  let fixture: ComponentFixture<ArticleOlyslagerPopupComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ArticleOlyslagerPopupComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ArticleOlyslagerPopupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
