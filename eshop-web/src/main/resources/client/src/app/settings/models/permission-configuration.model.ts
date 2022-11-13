export class PermissionConfigurationModel {
    permission: string;
    langKey: string;
    permissionId: number;
    enable: boolean;
    editable: boolean;

    constructor(data?: any | PermissionConfigurationModel) {
        if (!data) {
            return;
        }

        this.permission = data.permission;
        this.langKey = data.langKey;
        this.permissionId = data.permissionId;
        this.enable = data.enable;
        this.editable = data.editable;
    }
}
