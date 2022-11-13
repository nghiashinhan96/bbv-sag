import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ArticleResultContainerComponent } from './components/article-result-container/article-result-container.component';

const routes: Routes = [
    {
        path: 'result',
        component: ArticleResultContainerComponent
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class ArticleNotContextResultListRoutingModule { }
