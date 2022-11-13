import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MarginImportModalComponent } from './margin-import-modal.component';

xdescribe('MarginImportModalComponent', () => {
  let component: MarginImportModalComponent;
  let fixture: ComponentFixture<MarginImportModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MarginImportModalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MarginImportModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
