import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { map } from 'rxjs/internal/operators/map';
import { ErrorCodeEnum } from 'src/app/core/enums/error-code.enum';
import { Observable } from 'rxjs';

interface ConnectError {
    code: string;
    message: string;
}
@Component({
    selector: 'connect-error',
    templateUrl: './error.component.html',
    styleUrls: ['./error.component.scss']
})
export class ErrorComponent implements OnInit {

    error$: Observable<ConnectError>;
    constructor(private activatedRoute: ActivatedRoute) { }

    ngOnInit() {
        this.error$ = this.activatedRoute.queryParams.pipe(map(({ params }) => {
            const error: ConnectError = {
                code: params && params.code,
                message: ''
            };
            switch (params && (params.code || params.status)) {
                case ErrorCodeEnum.NOT_FOUND:
                    error.message = '';
                    break;
                case ErrorCodeEnum.SERVER_ERROR:
                case ErrorCodeEnum.UNKNOWN_ERROR:
                    error.message = 'Something went wrong, please try again!';
                    break;
            }
            return error;
        }));
    }

}
