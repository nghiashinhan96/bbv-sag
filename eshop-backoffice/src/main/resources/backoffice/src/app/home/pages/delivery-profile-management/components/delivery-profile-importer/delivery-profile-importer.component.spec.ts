import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DeliveryProfileImporterComponent } from './delivery-profile-importer.component';

xdescribe('DeliveryProfileImporterComponent', () => {
  let component: DeliveryProfileImporterComponent;
  let fixture: ComponentFixture<DeliveryProfileImporterComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DeliveryProfileImporterComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DeliveryProfileImporterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
