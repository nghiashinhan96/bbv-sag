export class ArticleImage {
    id_img: number;
    img_typ: string;
    ref: string;
    sort: number;

    constructor(data?) {
        if (data) {
            this.id_img = data.id_img;
            this.img_typ = data.img_typ;
            this.ref = data.ref;
            this.sort = data.sort;
        }
    }
}
