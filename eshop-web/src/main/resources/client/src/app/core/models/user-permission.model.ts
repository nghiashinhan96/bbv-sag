import { UserPermissionFunction } from './user-permission-function.model';

export class UserPermission {
    id: number;
    description: string;
    permission: string;
    createdBy: number;
    modifiedBy: any;
    functions: UserPermissionFunction[];

    constructor(data?: any) {
        if (data) {
            this.id = data.id;
            this.description = data.description;
            this.permission = data.permission;
            this.createdBy = data.createdBy;
            this.modifiedBy = data.modifiedBy;
            this.functions = (data.functions || []).map((func: any) => new UserPermissionFunction(func));
        }
    }
}
