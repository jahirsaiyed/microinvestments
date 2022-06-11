import dayjs from 'dayjs/esm';
import { IInvestor } from 'app/entities/investor/investor.model';

export interface IInvestorPortfolio {
  id?: number;
  units?: number | null;
  currentUnitPrice?: number | null;
  balance?: number | null;
  currentInvestedAmount?: number | null;
  createdOn?: dayjs.Dayjs | null;
  updatedOn?: dayjs.Dayjs | null;
  investor?: IInvestor | null;
}

export class InvestorPortfolio implements IInvestorPortfolio {
  constructor(
    public id?: number,
    public units?: number | null,
    public currentUnitPrice?: number | null,
    public balance?: number | null,
    public currentInvestedAmount?: number | null,
    public createdOn?: dayjs.Dayjs | null,
    public updatedOn?: dayjs.Dayjs | null,
    public investor?: IInvestor | null
  ) {}
}

export function getInvestorPortfolioIdentifier(investorPortfolio: IInvestorPortfolio): number | undefined {
  return investorPortfolio.id;
}
