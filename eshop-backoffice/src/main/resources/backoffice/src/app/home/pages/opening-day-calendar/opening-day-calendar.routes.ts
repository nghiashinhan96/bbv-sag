import { ModuleWithProviders } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { OpeningDayPageComponent } from './components/opening-day-page/opening-day-page.component';
import { OpeningDayFormPageComponent } from './components/opening-day-form-page/opening-day-form-page.component';
import { OpeningDayDetailResolverService } from './service/opening-day-detail-resolver.service';
import { OpeningDayListResolverService } from './service/opening-day-list-resolver.service';

const routes: Routes = [
    {
        path: '',
        component: OpeningDayPageComponent,
        resolve: {
            openingDayList: OpeningDayListResolverService
        }
    },
    {
        path: 'create',
        component: OpeningDayFormPageComponent,
        resolve: {
            openingDay: OpeningDayDetailResolverService
        }
    },
    {
        path: 'edit/:id',
        component: OpeningDayFormPageComponent,
        resolve: {
            openingDay: OpeningDayDetailResolverService
        }
    }
];

export const OpeningDayCalendarRoutingModule: ModuleWithProviders = RouterModule.forChild(routes);
