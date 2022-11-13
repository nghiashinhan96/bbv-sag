import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FooterComponent } from './footer.component';

describe('FooterComponent', () => {
  let component: FooterComponent;
  let fixture: ComponentFixture<FooterComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [FooterComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FooterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should correct the text display', () => {
    const element: HTMLElement = fixture.elementRef.nativeElement;
    const contentEl: HTMLElement = element.querySelector('container-xl');
    expect(contentEl.innerText).toEqual(`Allgemeine Geschäftsbedingungen
    Datenschutz
    Haftungsausschluss
    SAG Austria Handels GmbH, Österreich - Ein Unternehmen der Swiss Automotive Group AG`);
  });
});
