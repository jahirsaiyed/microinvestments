import { IInvestor } from 'app/entities/investor/investor.model';

export interface IInvestorAccount {
  id?: number;
  accountNo?: string | null;
  iBAN?: string | null;
  type?: string | null;
  walletAddress?: string | null;
  walletNetwork?: string | null;
  investor?: IInvestor | null;
}

export class InvestorAccount implements IInvestorAccount {
  constructor(
    public id?: number,
    public accountNo?: string | null,
    public iBAN?: string | null,
    public type?: string | null,
    public walletAddress?: string | null,
    public walletNetwork?: string | null,
    public investor?: IInvestor | null
  ) {}
}

export function getInvestorAccountIdentifier(investorAccount: IInvestorAccount): number | undefined {
  return investorAccount.id;
}
