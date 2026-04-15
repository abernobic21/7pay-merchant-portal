export interface Terminal {
  tid: string;
  terminalType: string;
  status: string;
  midReference: string;
  merchantCategoryCode: string;
  currency: string;
}

export interface TerminalDetail {
  tid: string;
  terminalType: string;
  status: string;
  midReference: string;
  phoneNumber: string;
  emailContact: string;
  currency: string;
  merchantCategoryCode: string;
  authenticationMode: string;
  softPosId: string;
  softPosStatus: string;
  tipEnabled: boolean;
  acquirerTerminalData: AcquirerTerminalData[];
}

export interface AcquirerTerminalData {
  acquirerTid: string;
  acquirerHost: string;
  transactionTypes: string[];
  cardBrands: string[];
}

export interface TerminalFilters {
  page: number;
  size: number;
  sort?: string;
  merchantOib?: string;
  mid?: string;
}
