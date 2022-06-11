import dayjs from 'dayjs/esm';

export interface IAssetDetailsAudit {
  id?: number;
  units?: number | null;
  unitPrice?: number | null;
  balance?: number | null;
  currentInvestedAmount?: number | null;
  profitLoss?: number | null;
  createdOn?: dayjs.Dayjs | null;
  updatedOn?: dayjs.Dayjs | null;
}

export class AssetDetailsAudit implements IAssetDetailsAudit {
  constructor(
    public id?: number,
    public units?: number | null,
    public unitPrice?: number | null,
    public balance?: number | null,
    public currentInvestedAmount?: number | null,
    public profitLoss?: number | null,
    public createdOn?: dayjs.Dayjs | null,
    public updatedOn?: dayjs.Dayjs | null
  ) {}
}

export function getAssetDetailsAuditIdentifier(assetDetailsAudit: IAssetDetailsAudit): number | undefined {
  return assetDetailsAudit.id;
}
