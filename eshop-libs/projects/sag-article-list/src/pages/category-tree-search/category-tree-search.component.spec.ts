import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SagLibCategoryTreeSearchComponent } from './category-tree-search.component';

xdescribe('CategoryTreeSearchComponent', () => {
  let component: SagLibCategoryTreeSearchComponent;
  let fixture: ComponentFixture<SagLibCategoryTreeSearchComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SagLibCategoryTreeSearchComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SagLibCategoryTreeSearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
