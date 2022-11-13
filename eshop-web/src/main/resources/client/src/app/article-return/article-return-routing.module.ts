import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ArticleReturnContainerComponent } from './pages/article-return-container/article-return-container.component';
import { ArticleReturnListComponent } from './pages/article-return-list/article-return-list.component';
import { ArticleReturnConfirmationComponent } from './pages/article-return-confirmation/article-return-confirmation.component';


const routes: Routes = [{
    path: '',
    component: ArticleReturnContainerComponent,
    children: [
        {
            path: '',
            pathMatch: 'full',
            redirectTo: 'basket'
        },
        {
            path: 'basket',
            component: ArticleReturnListComponent
        },
        {
            path: 'confirmation',
            component: ArticleReturnConfirmationComponent
        }
    ]
}];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class ArticleReturnRoutingModule { }
