import dayjs from 'dayjs/esm';
import { IInvestor } from 'app/entities/investor/investor.model';
import { TRANSACTIONTYPE } from 'app/entities/enumerations/transactiontype.model';

export interface ITransaction {
  id?: number;
  type?: TRANSACTIONTYPE | null;
  amount?: number | null;
  units?: number | null;
  unitPrice?: number | null;
  createdOn?: dayjs.Dayjs | null;
  updatedOn?: dayjs.Dayjs | null;
  investor?: IInvestor | null;
}

export class Transaction implements ITransaction {
  constructor(
    public id?: number,
    public type?: TRANSACTIONTYPE | null,
    public amount?: number | null,
    public units?: number | null,
    public unitPrice?: number | null,
    public createdOn?: dayjs.Dayjs | null,
    public updatedOn?: dayjs.Dayjs | null,
    public investor?: IInvestor | null
  ) {}
}

export function getTransactionIdentifier(transaction: ITransaction): number | undefined {
  return transaction.id;
}
