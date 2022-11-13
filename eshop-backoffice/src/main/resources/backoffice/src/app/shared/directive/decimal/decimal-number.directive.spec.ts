import { TestBed, ComponentFixture } from "@angular/core/testing";
import { DecimalNumberDirective } from "./decimal-number.directive";
import { Component, DebugElement } from "@angular/core";
import { By } from "@angular/platform-browser";
import { FormsModule } from "@angular/forms";
import { DecimalNumberService } from "./decimal-number.service";
// import { EnvUtil } from '../../../core/utils';

@Component({
  template: `<input type="text" [appDecimalNumber] [(ngModel)]="value" />`,
})
class TestDecimalNumberComponent {
  value;
}

fxdescribe("Directive: DecimalNumberDirective", () => {
  // let component: TestDecimalNumberComponent;
  // let fixture: ComponentFixture<TestDecimalNumberComponent>;
  // let inputEl: DebugElement;
  // let setting = EnvUtil.getDecimalSetting();
  // beforeEach(() => {
  //     TestBed.configureTestingModule({
  //         imports: [FormsModule],
  //         declarations: [DecimalNumberDirective, TestDecimalNumberComponent],
  //         providers: [DecimalNumberService]
  //     });
  //     fixture = TestBed.createComponent(TestDecimalNumberComponent);
  //     component = fixture.componentInstance;
  //     inputEl = fixture.debugElement.query(By.css('input'));
  // });
  // it('Directive create successfully', () => {
  //     expect(component).toBeTruthy();
  // });
  // it('Formatted data', () => {
  //     component.value = 112233.952;
  //     fixture.detectChanges();
  //     fixture.whenStable().then(() => {
  //         const val = inputEl.nativeElement.value;
  //         const expectedVal = `112${setting.thousandSeparator}233${setting.decimalSeparator}95`;
  //         expect(val).toEqual(expectedVal, 'Value correct the sparator, thousand separator');
  //     });
  // });
  // it('Formatted invalid data', () => {
  //     component.value = '11a3b.999';
  //     fixture.detectChanges();
  //     fixture.whenStable().then(() => {
  //         const val = inputEl.nativeElement.value;
  //         const expectedVal = `0${setting.decimalSeparator}00`;
  //         expect(val).toEqual(expectedVal, 'Value should remove invalid charactors');
  //     });
  // });
  // it('Formatted integer data', () => {
  //     component.value = 1234;
  //     inputEl.nativeElement.value = '1234';
  //     fixture.detectChanges();
  //     fixture.whenStable().then(() => {
  //         const val = inputEl.nativeElement.value;
  //         const expectedVal = `1${setting.thousandSeparator}234${setting.decimalSeparator}00`;
  //         expect(val).toEqual(expectedVal, 'Value should remove invalid charactors');
  //     });
  // });
});
