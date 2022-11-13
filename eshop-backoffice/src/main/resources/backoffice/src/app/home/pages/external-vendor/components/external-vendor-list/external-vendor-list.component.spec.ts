import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { RouterTestingModule } from '@angular/router/testing';
import { SharedModule } from '@app/shared/shared.module';
import { SelectModule } from 'ng-select';
import { TranslateModule, TranslateService } from 'ng2-translate';
import { ExternalVendorService } from '../../services/external-vendor.service';
import { ExternalVendorListComponent } from './external-vendor-list.component';


export class MockExternalVendorService {

}

export class MockTranslateService extends TranslateService {

}

xdescribe('ExternalVendorListComponent', () => {
    let component: ExternalVendorListComponent;
    let fixture: ComponentFixture<ExternalVendorListComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            imports: [SharedModule, SelectModule, RouterTestingModule.withRoutes([]), TranslateModule.forRoot()],
            declarations: [ExternalVendorListComponent],
            providers: [{
                provide: ExternalVendorService,
                useClass: MockExternalVendorService

            },
            {
                provide: TranslateService,
                useClass: MockTranslateService
            }]
        }).compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(ExternalVendorListComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });

    it('should create table', () => {
        fixture.whenStable().then(() => {
            const table = fixture.debugElement.query(By.css('.table'));
            const header = fixture.debugElement.query(By.css('thead'));
            expect(table).toBeTruthy();
            expect(header).toBeTruthy();
            expect(header.children.length).toEqual(2);
            expect(header.children[0].children.length).toEqual(8);
        });
    });

    it('should have button', () => {
        fixture.whenStable().then(() => {
            const importButton = fixture.debugElement.query(By.css('.fa-upload'));
            const createButton = fixture.debugElement.query(By.css('.fa-plus'));
            expect(importButton).toBeTruthy();
            expect(createButton).toBeTruthy();
        });
    });
});
