import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SagAuthFooterComponent } from './auth-footer.component';

xdescribe('AuthFooterComponent', () => {
  let component: SagAuthFooterComponent;
  let fixture: ComponentFixture<SagAuthFooterComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SagAuthFooterComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SagAuthFooterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
