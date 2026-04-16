export type TransactionType =
  | "sale"
  | "sale_with_installments"
  | "refund"
  | "storno"
  | "technical_reversal";

export type TransactionStatus = "authorized" | "rejected" | "no_response";

export type CardBrand =
  | "mastercard"
  | "visa"
  | "amex"
  | "maestro"
  | "diners"
  | "discover";

export interface Transaction {
  transactionId: string;
  transactionType: TransactionType;
  amount: number;
  currency: string;
  authorizationState: TransactionStatus;
  validity: string;
  cardBrand: CardBrand;
  acquirerHost: string;
  authNumber: string;
  rrn: string;
  timestamp: string;
}

export interface TransactionDetail {
  transactionId: string;
  transactionType: TransactionType;
  amount: number;
  currency: string;
  validity: string;
  authorizationState: TransactionStatus;
  responseCode: string;
  authNumber: string;
  acquirerHost: string;
  acquirerTid: string;
  acquirerMid: string;
  timestamp: string;
  panMasked: string;
  panExpireDate: string;
  cardBrand: CardBrand;
  amount2: number;
  currency2: string;
  stan: string;
  acquirerStan: string;
  rrn: string;
  acquirerRrn: string;
  tid: string;
  mid: string;
  paymentType: string;
  installmentNumber: number;
  transactionStateMDCProcessing: string;
  posTimestamp: string;
  acquirerTimestamp: string;
  transactionReference: string;
  tipAmount: number;
}

export interface TransactionFilters {
  merchantOib: string;
  timeFrom: string;
  timeUntil: string;

  page: number;
  size: number;
  sort?: string;
  mid?: string;
  tid?: string;
  status?: TransactionStatus;
  transactionType?: TransactionType;
  cardBrand?: CardBrand;
  authNumber?: string;
  rrn?: string;
}
