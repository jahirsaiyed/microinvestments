import dayjs from 'dayjs/esm';
import { PROMOTIONTYPE } from 'app/entities/enumerations/promotiontype.model';

export interface IPromotions {
  id?: number;
  description?: string | null;
  type?: PROMOTIONTYPE | null;
  amount?: number | null;
  createdOn?: dayjs.Dayjs | null;
  updatedOn?: dayjs.Dayjs | null;
}

export class Promotions implements IPromotions {
  constructor(
    public id?: number,
    public description?: string | null,
    public type?: PROMOTIONTYPE | null,
    public amount?: number | null,
    public createdOn?: dayjs.Dayjs | null,
    public updatedOn?: dayjs.Dayjs | null
  ) {}
}

export function getPromotionsIdentifier(promotions: IPromotions): number | undefined {
  return promotions.id;
}
