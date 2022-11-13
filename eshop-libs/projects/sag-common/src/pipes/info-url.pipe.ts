import { PipeTransform, Pipe } from '@angular/core';
import { UrlUtil } from '../utils/url.util';

@Pipe({name: 'infoUrl'})
export class InfoUrlPipe implements PipeTransform {

    transform(value: any, args: string[]): any {
        if (!args) {
            return UrlUtil.parseUrlStr(value, ['text-primary'], ['_blank']);
        }
        return UrlUtil.parseUrlStr(value, ['text-primary'], args);
    }
}
