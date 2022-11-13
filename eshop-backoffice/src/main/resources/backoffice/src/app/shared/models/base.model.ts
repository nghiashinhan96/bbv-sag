export abstract class BaseModel {
    id: number;

    constructor(data) {
         this.bind(data);
    }

    public bind(data) {
        data = data || {};
        for (let k of Object.keys(data)) {
            let v = data[k];
            if (typeof v === 'function') {
                continue;
            }
            this[k] = v;
        }
    }

    public getId() {
        return this.id;
    }
}

