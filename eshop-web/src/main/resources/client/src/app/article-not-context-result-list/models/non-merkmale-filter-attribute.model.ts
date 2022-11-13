export class NonMerkmaleAttribute {
    searchType: string;
    attributes: { key: string, value: string }[] = [];

    constructor(data?: any) {
        if (data) {
            this.searchType = data.type;
        }
    }

    toTyreAttribute(attribute: any) {
        for (const key in attribute) {
            if (attribute.hasOwnProperty(key)) {
                const element = attribute[key] || '';
                if (!!element.trim()) {
                    if (key === 'season') {
                        this.attributes.push({
                            key: 'SEARCH.SEARCH_TYRES.SEASON',
                            value: `TYRE.SEASONS.${element}`
                        });
                    } else if (key === 'width') {
                        this.attributes.push({
                            key: 'SEARCH.SEARCH_TYRES.WIDTH',
                            value: element
                        });
                    } else if (key === 'height') {
                        this.attributes.push({
                            key: 'SEARCH.SEARCH_TYRES.CROSS_SECTION',
                            value: element
                        });
                    } else if (key === 'radius') {
                        this.attributes.push({
                            key: 'SEARCH.SEARCH_TYRES.DIAMETER',
                            value: element
                        });
                    } else if (key === 'speedIndex') {
                        this.attributes.push({
                            key: 'SEARCH.SEARCH_TYRES.SPEED_INDEX',
                            value: element
                        });
                    } else if (key === 'loadIndex') {
                        this.attributes.push({
                            key: 'SEARCH.SEARCH_TYRES.LOAD_INDEX',
                            value: element
                        });
                    } else if (key === 'tyreSegment') {
                        this.attributes.push({
                            key: 'SEARCH.SEARCH_TYRES.TYRE_SEGMENT',
                            value: `TYRE.TYRE_SEGMENTS.${element && element.toUpperCase()}`
                        });
                    } else if (key === 'supplier') {
                        this.attributes.push({
                            key: 'ARTICLE_FILTER.SUPPLIERS',
                            value: element
                        });
                    } else if (key === 'fzCategory') {
                        this.attributes.push({
                            key: 'SEARCH.SEARCH_TYRES.FZ_CATEGORY',
                            value: element
                        });
                    } else if (key === 'runflat' && this.parseBoolean(element)) {
                        this.attributes.push({
                            key: 'SEARCH.SEARCH_TYRES.RUNFLAT',
                            value: 'COMMON_LABEL.YES'
                        });
                    } else if (key === 'spike' && this.parseBoolean(element)) {
                        this.attributes.push({
                            key: 'SEARCH.SEARCH_TYRES.SPIKE',
                            value: 'COMMON_LABEL.YES'
                        });
                    } else if (key === 'category') {
                        this.attributes.push({
                            key: 'SEARCH.SEARCH_TYRES.MOTOR_CATEGORY',
                            value: `TYRE.MOTOR_CATEGORIES.${element}`
                        });
                    } else if (key === 'matchCode') {
                        this.attributes.push({
                            key: 'SEARCH.SEARCH_TYRES.MATCH_CODE',
                            value: element
                        });
                    }
                }
            }
        }
        return this;
    }

    toBatteryAttribute(attribute: any) {
        for (const key in attribute) {
            if (attribute.hasOwnProperty(key)) {
                const element = attribute[key];
                const isRangeField = key === 'ampereHour' || key === 'length' || key === 'width' || key === 'height';
                if (key === 'voltage') {
                    this.attributes.push({
                        key: 'BATTERIES.VOLTAGES',
                        value: element
                    });
                } else if (key === 'typeOfPole') {
                    this.attributes.push({
                        key: 'BATTERIES.TYPES_OF_POLE',
                        value: element
                    });
                } else if (isRangeField) {
                    const values: string[] = element.split(',');
                    let inRangeValuesDisplayText = '';
                    if (values.length > 1) {
                        inRangeValuesDisplayText = values[0] + ' - ' + values[values.length - 1];
                    } else {
                        inRangeValuesDisplayText = values[0];
                    }
                    const translateKey = this.getTranslateKey(key);
                    this.attributes.push({
                        key: translateKey,
                        value: inRangeValuesDisplayText
                    });
                } else if (key === 'interconnection') {
                    this.attributes.push({
                        key: 'BATTERIES.CIRCUIT',
                        value: element
                    });
                } else if (key === 'withoutStartStop' && this.parseBoolean(element)) {
                    this.attributes.push({
                        key: 'BATTERIES.WITHOUT_START_STOP',
                        value: element
                    });
                } else if (key === 'withStartStop' && this.parseBoolean(element)) {
                    this.attributes.push({
                        key: 'BATTERIES.WITH_START_STOP',
                        value: 'COMMON_LABEL.YES'
                    });
                }
            }
        }
        return this;
    }

    toBulbAttribute(attribute: any) {
        for (const key in attribute) {
            if (attribute.hasOwnProperty(key)) {
                const element = attribute[key];
                if (key === 'code') {
                    this.attributes.push({
                        key: 'BULBS.CODE',
                        value: element
                    });
                } else if (key === 'voltage') {
                    this.attributes.push({
                        key: 'BULBS.VOLTAGE',
                        value: element
                    });
                } else if (key === 'watt') {
                    this.attributes.push({
                        key: 'BULBS.WATT',
                        value: element
                    });
                } else if (key === 'supplier') {
                    this.attributes.push({
                        key: 'BULBS.SUPPLIER',
                        value: element
                    });
                }
            }
        }
        return this;
    }

    toOilAttribute(attribute: any) {
        for (const key in attribute) {
            if (attribute.hasOwnProperty(key)) {
                const element = attribute[key];
                if (key === 'vehicle') {
                    this.attributes.push({
                        key: 'OIL.VEHICLE',
                        value: element
                    });
                } else if (key === 'aggregate') {
                    this.attributes.push({
                        key: 'OIL.AGGREGATE',
                        value: element
                    });
                } else if (key === 'viscosity') {
                    this.attributes.push({
                        key: 'OIL.VISCOSITY',
                        value: element
                    });
                } else if (key === 'bottleSize') {
                    const bottleSizes: string[] = element.split(',');
                    let inRangeValuesDisplayText = '';
                    if (bottleSizes.length > 1) {
                        inRangeValuesDisplayText = bottleSizes[0] + ' - ' + bottleSizes[bottleSizes.length - 1];
                    } else {
                        inRangeValuesDisplayText = bottleSizes[0];
                    }
                    this.attributes.push({
                        key: 'OIL.BOTTLE_SIZE',
                        value: inRangeValuesDisplayText
                    });
                } else if (key === 'approved') {
                    this.attributes.push({
                        key: 'OIL.APPROVED',
                        value: element
                    });
                } else if (key === 'specification') {
                    this.attributes.push({
                        key: 'OIL.SPECIFICATION',
                        value: element
                    });
                } else if (key === 'brand') {
                    this.attributes.push({
                        key: 'OIL.BRAND',
                        value: element
                    });
                }
            }
        }
        return this;
    }

    private parseBoolean(element) {
        try {
            const val = JSON.parse(element);
            return val;
        } catch (ex) {
            return false;
        }
    }

    private getTranslateKey(key) {
        switch (key) {
            case 'ampereHour':
                return 'BATTERIES.AMPERES';
            case 'length':
                return 'COMMON_LABEL.LENGTH';
            case 'width':
                return 'COMMON_LABEL.WIDTH';
            case 'height':
                return 'COMMON_LABEL.HEIGHT';
        }
    }
}
