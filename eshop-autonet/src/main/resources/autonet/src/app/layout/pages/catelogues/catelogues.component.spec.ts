import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CateloguesComponent } from './catelogues.component';

xdescribe('CateloguesComponent', () => {
  let component: CateloguesComponent;
  let fixture: ComponentFixture<CateloguesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CateloguesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CateloguesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
