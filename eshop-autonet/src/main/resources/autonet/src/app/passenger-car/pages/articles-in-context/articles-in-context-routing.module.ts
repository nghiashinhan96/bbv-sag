import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ArticleInContextComponent } from './articles-in-context.component';
import { QuickClickComponent } from './pages/quick-click/quick-click.component';
import { ArticleInContextListComponent } from './pages/article-in-context-list/article-in-context-list.component';


const routes: Routes = [{
    path: '',
    component: ArticleInContextComponent,
    children: [
        {
            path: '',
            pathMatch: 'full',
            redirectTo: 'quick-click'
        },
        {
            path: 'quick-click',
            component: QuickClickComponent
        },
        {
            path: 'articles',
            component: ArticleInContextListComponent
        }
    ]
}];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class ArticlesInContextRoutingModule { }
