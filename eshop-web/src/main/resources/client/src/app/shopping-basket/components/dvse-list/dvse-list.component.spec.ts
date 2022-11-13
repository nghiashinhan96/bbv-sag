import { ComponentFixture, TestBed, async } from '@angular/core/testing';

import { DvseListComponent } from './dvse-list.component';
import { RouterTestingModule } from '@angular/router/testing';
import { TranslateModule } from '@ngx-translate/core';

xdescribe('DvseListComponent', () => {
  let component: DvseListComponent;
  let fixture: ComponentFixture<DvseListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DvseListComponent],
      imports:[
        TranslateModule.forRoot(),
        RouterTestingModule
      ]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DvseListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
