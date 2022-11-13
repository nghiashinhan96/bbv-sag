export class CategoryTile {
    id: string;
    tileExternals: CategoryExternalTile[];
    tileImage: string;
    tileName: string;
    tileNodeLink: string;
    tileSort: string;
    tileNodeId: string;
    tileType: string;

    linkNotHightLighted: CategoryExternalTile[] = [];
    linkHightLighted: CategoryExternalTile[] = [];

    constructor(data = null) {
        if(data) {
            Object.keys(data).forEach(key => {
                this[key] = data[key];
            });

            if(data.tileExternals) {
                this.tileExternals = (data.tileExternals || []).map(item => new CategoryExternalTile(item));
            }

            this.linkNotHightLighted = this.getLinkNotHightLighted();
            this.linkHightLighted = this.getLinkHightLighted();
        }
    }

    getLinkNotHightLighted() {
        return this.tileExternals.filter(link => !link.tileLinkHighlighted);
    }

    getLinkHightLighted() {
        return this.tileExternals.filter(link => link.tileLinkHighlighted);
    }
}

export class CategoryExternalTile {
    tileLink: string;
    tileLinkSort: string;
    tileLinkText: string;
    tileLinkType: string;
    tileLinkAttr: string;
    tileLinkHighlighted: boolean;

    constructor(data = null) {
        if(data) {
            Object.keys(data).forEach(key => {
                this[key] = data[key];
            });
        }
    }
}