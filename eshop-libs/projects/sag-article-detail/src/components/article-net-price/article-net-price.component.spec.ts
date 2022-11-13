import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SagArticleNetPriceComponent } from './article-net-price.component';

xdescribe('ArticleNetPriceComponent', () => {
  let component: SagArticleNetPriceComponent;
  let fixture: ComponentFixture<SagArticleNetPriceComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SagArticleNetPriceComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SagArticleNetPriceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
