import dayjs from 'dayjs/esm';
import { PROMOTIONTYPE } from 'app/entities/enumerations/promotiontype.model';

export interface IPromotionsAudit {
  id?: number;
  description?: string | null;
  type?: PROMOTIONTYPE | null;
  amount?: number | null;
  createdOn?: dayjs.Dayjs | null;
  updatedOn?: dayjs.Dayjs | null;
}

export class PromotionsAudit implements IPromotionsAudit {
  constructor(
    public id?: number,
    public description?: string | null,
    public type?: PROMOTIONTYPE | null,
    public amount?: number | null,
    public createdOn?: dayjs.Dayjs | null,
    public updatedOn?: dayjs.Dayjs | null
  ) {}
}

export function getPromotionsAuditIdentifier(promotionsAudit: IPromotionsAudit): number | undefined {
  return promotionsAudit.id;
}
