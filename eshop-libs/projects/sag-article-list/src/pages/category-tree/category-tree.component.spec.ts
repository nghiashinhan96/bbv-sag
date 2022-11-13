import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SagLibCategoryTreeComponent } from './category-tree.component';

xdescribe('CategoryTreeComponent', () => {
  let component: SagLibCategoryTreeComponent;
  let fixture: ComponentFixture<SagLibCategoryTreeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SagLibCategoryTreeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SagLibCategoryTreeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
