import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AuthenticationComponent } from './authentication.component';
import { ErrorComponent } from './pages/error/error.component';


const routes: Routes = [
    {
        path: 'unauthorized',
        component: AuthenticationComponent,
    }, {
        path: '**',
        redirectTo: 'unauthorized'
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class AuthenticationRoutingModule { }
