import { Constant } from 'src/app/core/conts/app.constant';

export class BulbFilter {
  supplier: string = Constant.SPACE;
  voltage: string = Constant.SPACE;
  watt: string = Constant.SPACE;
  code: string = Constant.SPACE;
  totalElements = 0;
}
