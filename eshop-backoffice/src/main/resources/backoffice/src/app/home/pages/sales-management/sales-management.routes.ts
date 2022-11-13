import { Routes, RouterModule } from '@angular/router';
import { ModuleWithProviders } from '@angular/core';
import { SalesListComponent } from './components/sales-list/sales-list.component';
import { SalesCreatingComponent } from './components/sales-creating/sales-creating.component';
import { SalesEditingComponent } from './components/sales-editing/sales-editing.component';
import { CanDeactivateGuard } from 'src/app/core/services/deactive-guard.service';
import { HomeAuthResolver } from '../../home-auth-resolver.service';


export const routes: Routes = [
    {
        path: '',
        component: SalesListComponent,
        resolve: {
            isAuthed: HomeAuthResolver
        }
    },
    {
        path: 'create',
        component: SalesCreatingComponent,
        resolve: {
            isAuthed: HomeAuthResolver
        },
        canDeactivate: [CanDeactivateGuard]
    },
    {
        path: 'edit/:id',
        component: SalesEditingComponent,
        resolve: {
            isAuthed: HomeAuthResolver
        },
        canDeactivate: [CanDeactivateGuard]
    }
];

export const SalesManagementRoutes: ModuleWithProviders = RouterModule.forChild(routes);
