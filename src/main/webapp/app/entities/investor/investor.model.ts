import dayjs from 'dayjs/esm';
import { IInvestorPortfolio } from 'app/entities/investor-portfolio/investor-portfolio.model';
import { IInvestorAccount } from 'app/entities/investor-account/investor-account.model';
import { ITransaction } from 'app/entities/transaction/transaction.model';
import { Gender } from 'app/entities/enumerations/gender.model';

export interface IInvestor {
  id?: number;
  name?: string;
  email?: string;
  gender?: Gender;
  phone?: string;
  addressLine1?: string;
  addressLine2?: string | null;
  city?: string;
  country?: string;
  createdOn?: dayjs.Dayjs | null;
  portfolio?: IInvestorPortfolio | null;
  accounts?: IInvestorAccount[] | null;
  transactions?: ITransaction[] | null;
}

export class Investor implements IInvestor {
  constructor(
    public id?: number,
    public name?: string,
    public email?: string,
    public gender?: Gender,
    public phone?: string,
    public addressLine1?: string,
    public addressLine2?: string | null,
    public city?: string,
    public country?: string,
    public createdOn?: dayjs.Dayjs | null,
    public portfolio?: IInvestorPortfolio | null,
    public accounts?: IInvestorAccount[] | null,
    public transactions?: ITransaction[] | null
  ) {}
}

export function getInvestorIdentifier(investor: IInvestor): number | undefined {
  return investor.id;
}
