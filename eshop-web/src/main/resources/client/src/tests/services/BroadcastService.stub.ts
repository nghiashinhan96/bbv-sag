import { of } from "rxjs";

export class BroadcastServiceStub {
    on() {
        return of({});
    }
}