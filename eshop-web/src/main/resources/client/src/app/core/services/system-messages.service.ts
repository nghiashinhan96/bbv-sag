import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { BehaviorSubject, of } from 'rxjs';
import { map, switchMap, catchError } from 'rxjs/operators';
import { SystemMessage } from '../models/system-message.model';

@Injectable({ providedIn: 'root' })
export class SystemMessagesService {
    messages: SystemMessage[] = [];
    private messagesSub$ = new BehaviorSubject<SystemMessage[]>([]);
    constructor(
        private http: HttpClient
    ) { }

    get messages$() {
        return this.messagesSub$.asObservable();
    }

    getSystemMessages(isAuthed: boolean, lang?) {
        let sub;
        if (isAuthed) {
            sub = this.http.get(`${environment.baseUrl}messages/own/${environment.affiliate}`);
        } else {
            sub = this.http.get(`${environment.baseUrl}messages/common/${environment.affiliate}/${lang}`);
        }
        return sub.pipe(
            switchMap((data: SystemMessage[]) => {
                const isEnableSSO = data.filter(d => !!d.ssoTraining).length > 0;
                if (isAuthed && isEnableSSO) {
                    return this.getSSOInfo().pipe(
                        map(info => {
                            if (info) {
                                data.forEach(d => {
                                    if (d.ssoTraining) {
                                        d.ssoInfo = info;
                                    }
                                });
                            }
                            this.messages = data;
                            this.messagesSub$.next(this.messages);
                            return data;
                        })
                    );
                }
                this.messages = data;
                this.messagesSub$.next(this.messages);
                return of(this.messages);
            })
        );
    }

    setHideMessageById(mesId) {
        return this.http.post(`${environment.baseUrl}messages/hide/${mesId}`, null);
    }

    getSSOInfo() {
        return this.http.get(`${environment.baseUrl}training/sso`).pipe(catchError(() => of(null)));
    }
}
