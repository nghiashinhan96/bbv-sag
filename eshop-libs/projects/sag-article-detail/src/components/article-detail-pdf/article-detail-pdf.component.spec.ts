import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ArticleDetailPdfComponent } from './article-detail-pdf.component';

xdescribe('ArticleDetailPdfComponent', () => {
  let component: ArticleDetailPdfComponent;
  let fixture: ComponentFixture<ArticleDetailPdfComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ArticleDetailPdfComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ArticleDetailPdfComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
