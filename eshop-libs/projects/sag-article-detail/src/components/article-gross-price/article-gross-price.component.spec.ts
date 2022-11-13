import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ArticleGrossPriceComponent } from './article-gross-price.component';

xdescribe('ArticleGrossPriceComponent', () => {
  let component: ArticleGrossPriceComponent;
  let fixture: ComponentFixture<ArticleGrossPriceComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ArticleGrossPriceComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ArticleGrossPriceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
