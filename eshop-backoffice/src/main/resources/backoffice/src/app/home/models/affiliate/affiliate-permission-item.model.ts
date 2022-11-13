export class AffiliatePermissionItem {
    editable: boolean;
    enable: boolean;
    langKey: string;
    permission: string;
    permissionId: number;
    constructor(json?: AffiliatePermissionItem) {
        if (!json) {
            return;
        }
        this.editable = json.editable;
        this.enable = json.enable;
        this.langKey = json.langKey;
        this.permission = json.permission;
        this.permissionId = json.permissionId;
    }
}
