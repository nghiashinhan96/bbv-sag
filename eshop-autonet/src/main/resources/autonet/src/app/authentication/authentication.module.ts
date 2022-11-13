import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { AuthenticationRoutingModule } from './authentication-routing.module';
import { AuthenticationComponent } from './authentication.component';
import { ErrorComponent } from './pages/error/error.component';
import { AuthenGuard } from '../authentication/guards/authen.guard';
import { TranslateModule } from '@ngx-translate/core';
import { TabsModule } from 'ngx-bootstrap/tabs';
@NgModule({
    declarations: [
        AuthenticationComponent,
        ErrorComponent
    ],
    imports: [
        CommonModule,
        AuthenticationRoutingModule,
        TranslateModule.forRoot(),
        TabsModule.forRoot(),
        ReactiveFormsModule
    ]
})
export class AuthenticationModule { }
