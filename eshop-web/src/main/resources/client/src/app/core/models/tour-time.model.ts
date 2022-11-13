export class TourTime {
    branchId: string;
    branchName: string;
    customerNumber: string;
    cutOffMinutes: string;
    id: number;
    tourDays: string;
    tourDepartureTime: string;
    tourName: string;

    tourDaysConvert: string[];
    tourDaysDisplay: string;

    displayBranchInfo: string;

    constructor(data?: any) {
        if (data) {
            this.branchId = data.branchId;
            this.branchName = data.branchName || '';
            this.customerNumber = data.customerNumber;
            this.cutOffMinutes = data.cutOffMinutes;
            this.id = data.id;
            this.tourDays = data.tourDays;
            this.tourDepartureTime = data.tourDepartureTime;
            this.tourName = data.tourName;
            this.tourDaysConvert = this.convertTourDays();
            this.displayBranchInfo = this.initBranchInfo();
        }
    }

    initBranchInfo() {
        return `${this.branchId} - ${this.branchName}`;
    }

    convertTourDays() {
        const data = this.tourDays && this.tourDays.split(',') || [];
        let displayData = [];
        if(data.length > 0) {
            const tourDayNumbers = this.initTourDaysNumber();
            displayData = data.map(dta => tourDayNumbers[dta]);
            return displayData;
        }

        return [];
    }

    initTourDaysNumber() {
        return {
            1: 'MON',
            2: 'TUE',
            3: 'WED',
            4: 'THU',
            5: 'FRI',
            6: 'SAT',
            7: 'SUN'
        };
    }
}