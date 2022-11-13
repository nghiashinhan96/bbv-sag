import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ArticleDetailManufacturerComponent } from './article-detail-manufacturer.component';

xdescribe('ArticleDetailManufacturerComponent', () => {
  let component: ArticleDetailManufacturerComponent;
  let fixture: ComponentFixture<ArticleDetailManufacturerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ArticleDetailManufacturerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ArticleDetailManufacturerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
