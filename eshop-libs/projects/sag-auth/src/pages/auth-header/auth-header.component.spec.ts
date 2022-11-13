import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SagAuthHeaderComponent } from './auth-header.component';

xdescribe('AuthHeaderComponent', () => {
  let component: SagAuthHeaderComponent;
  let fixture: ComponentFixture<SagAuthHeaderComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SagAuthHeaderComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SagAuthHeaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
