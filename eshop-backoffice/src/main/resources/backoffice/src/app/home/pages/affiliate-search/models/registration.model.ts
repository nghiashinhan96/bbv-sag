import { BaseModel } from 'src/app/shared/models/base.model';

export class RegistrationModel extends BaseModel {
  affiliate: string;
  userName: string;
  firstName: string;
  surName: string;
  email: string;
  collectionShortName: string;
  customerNumber: string;
  postCode: string;
  hasPartnerprogramView = true;
  constructor(data?: any) {
    super(data);
  }
}

export class CustomerCheckingModel extends BaseModel {
  customerNumber: string;
  affiliate: string;
  postCode: string;
  constructor(data?: any) {
    super(data);
  }
}

export interface NotificationModel {
  status: boolean;
  messages: string[];
}
