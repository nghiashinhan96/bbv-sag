import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { AuthenticationComponent } from './authentication.component';
import { ErrorComponent } from './pages/error/error.component';


const routes: Routes = [
    {
        path: 'login',
        component: AuthenticationComponent,
        canActivate: [],
        children: [
            {
                path: '',
                component: LoginComponent
            }
        ]
    }, {
        path: '**',
        component: ErrorComponent
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class AuthenticationRoutingModule { }
