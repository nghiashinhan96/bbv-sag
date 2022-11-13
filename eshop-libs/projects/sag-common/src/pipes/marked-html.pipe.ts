
import { Pipe, PipeTransform } from '@angular/core';

import * as marked from 'marked';
const markeredLib = marked;

@Pipe({
    name: 'markedHtml'
})
export class MarkedHtmlPipe implements PipeTransform {
    public transform(value: string): string {
        if(value) {
            return markeredLib ? markeredLib.parseInline(value) : value;
        }

        return '';
    }
}
