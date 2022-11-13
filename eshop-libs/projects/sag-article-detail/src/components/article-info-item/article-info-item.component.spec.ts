import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ArticleInfoItemComponent } from './article-info-item.component';

xdescribe('ArticleInfoItemComponent', () => {
  let component: ArticleInfoItemComponent;
  let fixture: ComponentFixture<ArticleInfoItemComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ArticleInfoItemComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ArticleInfoItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
