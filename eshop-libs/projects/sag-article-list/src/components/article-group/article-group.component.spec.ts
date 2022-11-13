import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ArticleGroupComponent } from './article-group.component';

xdescribe('ArticleGroupComponent', () => {
  let component: ArticleGroupComponent;
  let fixture: ComponentFixture<ArticleGroupComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ArticleGroupComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ArticleGroupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
