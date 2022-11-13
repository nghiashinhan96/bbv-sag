import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { AutonetCommonModule } from '../shared/autonet-common/autonet-common.module';
import { SingleArticleModule } from '../shared/single-article/single-article.module';

@NgModule({
    declarations: [],
    imports: [
        CommonModule,
        HttpClientModule,
        AutonetCommonModule,
        SingleArticleModule
    ]
})
export class CoreModule { }
