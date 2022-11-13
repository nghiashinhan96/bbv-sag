import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import {
    SagInContextResolver
} from 'sag-in-context';

import { ArticleInContextResultListComponent } from './articles-in-context.component';
import { ArticlesInContextQuickClickComponent } from './pages/quick-click/quick-click.component';
import { ArticlesInContextClassicCategoriesComponent } from './pages/classic-categories/classic-categories.component';
import { ArtilesInContextListComponent } from './pages/articles-in-context-list/article-in-context-list.component';
import { CzClassicGuard } from '../shared/cz-custom/guard/cz-classic.guard';

const routes: Routes = [
    {
        path: '',
        component: ArticleInContextResultListComponent,
        resolve: {
            data: SagInContextResolver
        },
        children: [
            {
                path: '',
                pathMatch: 'full',
                redirectTo: 'quick-click'
            },
            {
                path: 'quick-click',
                component: ArticlesInContextQuickClickComponent
            },
            {
                path: 'articles',
                component: ArtilesInContextListComponent
            },
            {
                path: 'classic',
                component: ArticlesInContextClassicCategoriesComponent,
                canActivate: [CzClassicGuard]
            }
        ]
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class ArticlesInContextRoutingModule { }
