import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { OrderHistoryDetailComponent } from './components/order-history-detail/order-history-detail.component';
import { ConfigComponent } from './pages/config/config.component';
import { OrderHistoryComponent } from './pages/order-history/order-history.component';
import { ProfileComponent } from './pages/profile/profile.component';
import { SettingsComponent } from './pages/settings/settings.component';
import { OrderHistoryDetailResolverService } from './services/order-history-detail-resolver.service';
import { InvoiceComponent } from './pages/invoice/invoice.component';
import { NormalAdminComponent } from './pages/normal-admin/normal-admin.component';
import { NormalAdminResolverService } from './services/normal-admin-resolver.service';
import { FinalUserAdminComponent } from './pages/final-user-admin/final-user-admin.component';
import { FinalUserAdminOrdersComponent } from './pages/final-user-admin-orders/final-user-admin-orders.component';
import { FinalUserAdminOrderDetailComponent } from './pages/final-user-admin-order-detail/final-user-admin-order-detail.component';
import { UserRoleGuard } from '../authentication/guard/user-role.guard';
import { USER_ROLE } from '../authentication/enums/user-role.enum';
import { SaleGuard } from '../authentication/guard/sale.guard';
import { PermissionGuard } from '../authentication/guard/permission.guard';
import { USER_PERMISSION } from '../authentication/enums/permission.enum';
import { AnalyticalCardComponent } from './pages/analytical-card/analytical-card.component';
import { AnalyticalCardDetailComponent } from './pages/analytical-card-detail/analytical-card-detail.component';


const routes: Routes = [
    {
        path: '',
        component: SettingsComponent,
        children: [
            {
                path: '',
                pathMatch: 'full',
                redirectTo: 'profile'
            },
            {
                path: 'order-history/:orderNumber',
                component: OrderHistoryDetailComponent,
                resolve: {
                    orderDetail: OrderHistoryDetailResolverService
                },
                data: {
                    role: {
                        name: USER_ROLE.FINAL_USER,
                        negative: true
                    }
                },
                canActivate: [UserRoleGuard]
            },
            {
                path: 'final-user-orders/:orderNumber',
                component: FinalUserAdminOrderDetailComponent,
                data: {
                    role: {
                        name: USER_ROLE.FINAL_USER
                    }
                },
                canActivate: [UserRoleGuard]
            },
            {
                path: 'profile',
                component: ProfileComponent
            },
            {
                path: 'order-history',
                component: OrderHistoryComponent,
                data: {
                    role: {
                        name: USER_ROLE.FINAL_USER,
                        negative: true
                    }
                },
                canActivate: [UserRoleGuard]
            },
            {
                path: 'final-user-orders',
                component: FinalUserAdminOrdersComponent,
                data: {
                    role: {
                        name: USER_ROLE.FINAL_USER
                    }
                },
                canActivate: [UserRoleGuard]
            },
            {
                path: 'invoices',
                component: InvoiceComponent,
                data: {
                    permission: USER_PERMISSION.USED_INVOICE_HISTORY
                },
                canActivate: [PermissionGuard]
            },
            {
                path: 'analytical-card',
                component: AnalyticalCardComponent
            },
            {
                path: 'analytical-card/detail',
                component: AnalyticalCardDetailComponent
            },
            {
                path: 'administrator',
                component: NormalAdminComponent,
                resolve: {
                    allUsers: NormalAdminResolverService
                },
                data: {
                    role: {
                        name: USER_ROLE.USER_ADMIN
                    },
                    isSalesOnBeHalf: false
                },
                canActivate: [UserRoleGuard, SaleGuard]
            },
            {
                path: 'final-user-admin',
                component: FinalUserAdminComponent,
                data: {
                    role: {
                        name: USER_ROLE.FINAL_USER_ADMIN
                    }
                },
                canActivate: [UserRoleGuard]
            },
            {
                path: 'configuration',
                component: ConfigComponent
            },
            {
                path: '**',
                component: ProfileComponent,
                redirectTo: '/settings/profile'
            }
        ]
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class SettingsRoutingModule { }
