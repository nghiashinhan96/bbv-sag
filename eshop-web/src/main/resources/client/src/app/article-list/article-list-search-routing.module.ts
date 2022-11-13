import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ArticleListSearchComponent } from './article-list-search.component';


const routes: Routes = [
    {
        path: '',
        component: ArticleListSearchComponent
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class ArticleListSearchRoutingModule { }
