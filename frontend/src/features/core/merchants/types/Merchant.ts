import type { Address } from "@/shared/types";

export interface Merchant {
  shortName: string;
  fullName: string;
  oib: string;
  address: Address;
  status: string;
}

export interface MerchantDetail {
  shortName: string;
  fullName: string;
  oib: string;
  address: Address;
  status: string;
  supportedTransactionTypes: string[];
  supportedAcquirers: string[];
  supportedCardBrands: string[];
}

export interface MerchantFilters {
  page: number;
  size: number;
  sort?: string;
  status?: string;
}
