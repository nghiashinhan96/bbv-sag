import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ArticlesNonContextComponent } from './articles-non-context.component';


const routes: Routes = [{
  path: '',
  component: ArticlesNonContextComponent
}];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ArticlesNonContextRoutingModule { }
