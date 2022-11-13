export class IncentivePoints {
    url: string;
    mode: string;
    description: string;
    showHappyPoints: boolean;
    points: number;
    acceptHappyPointTerm: boolean;

    constructor(res: any) {
        if (!res) {
            return;
        }
        this.url = res.data.url;
        this.mode = res.data.mode;
        this.description = res.data.description;
        this.points = res.data.points;
        this.showHappyPoints = res.data.showHappyPoints;
        this.acceptHappyPointTerm = res.data.acceptHappyPointTerm;
    }
}
