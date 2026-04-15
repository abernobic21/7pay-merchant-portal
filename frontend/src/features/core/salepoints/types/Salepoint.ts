import type { Address } from "@/shared/types";

export interface Salepoint {
  name: string;
  mid: string;
  address: Address;
  status: string;
  merchantOib: string;
}

export interface SalepointFilters {
  page: number;
  size: number;
  stmerchantOibatus?: string;
}

export interface SalepointDetail {
  name: string;
  mid: string;
  address: Address;
  status: string;
  merchantOib: string;
  acquirerMerchantData: AcquirerMerchantData[];
}

export interface AcquirerMerchantData {
  acquirerMid: string;
  acquirerHost: string;
  transactionTypes: string[];
  cardBrands: string[];
}
