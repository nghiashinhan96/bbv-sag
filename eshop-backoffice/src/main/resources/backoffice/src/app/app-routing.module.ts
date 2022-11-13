import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AuthenGuard } from './authentication/guards/authen.guard';

const routes: Routes = [
    {
        path: '',
        canActivate: [AuthenGuard],
        canActivateChild: [AuthenGuard],
        loadChildren: () => import('./layout/layout.module').then(m => m.LayoutModule)
    }
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule { }
