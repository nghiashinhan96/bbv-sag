import { Injectable } from '@angular/core';
import { Subject } from 'rxjs/internal/Subject';
import { Observable } from 'rxjs/internal/Observable';
import { filter } from 'rxjs/internal/operators/filter';
import { map } from 'rxjs/internal/operators/map';

export interface BroadcastEvent {
    key: any;
    data?: any;
}
@Injectable()
export class BroadcastService {
    private eventBus: Subject<BroadcastEvent>;

    constructor() {
        this.eventBus = new Subject<BroadcastEvent>();
    }

    broadcast(key: any, data?: any) {
        this.eventBus.next({ key, data });
    }

    on<T>(key: any): Observable<T> {
        return this.eventBus.asObservable().pipe(
            filter(event => event.key === key),
            map(event => event.data as T)
        );
    }
}
