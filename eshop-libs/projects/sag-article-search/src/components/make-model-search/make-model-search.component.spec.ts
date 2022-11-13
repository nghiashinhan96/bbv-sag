import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MakeModelSearchComponent } from './make-model-search.component';

describe('MakeModelSearchComponent', () => {
  let component: MakeModelSearchComponent;
  let fixture: ComponentFixture<MakeModelSearchComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MakeModelSearchComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MakeModelSearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
