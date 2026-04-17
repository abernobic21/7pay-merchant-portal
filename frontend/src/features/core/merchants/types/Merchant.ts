import type { Address } from "@/shared/types";
import type {
  CardBrand,
  TransactionType,
} from "../../transactions/types/Transaction";

export interface Merchant {
  shortName: string;
  fullName: string;
  oib: string;
  address: Address;
  status: MerchantStatus;
}

export interface MerchantDetail {
  shortName: string;
  fullName: string;
  oib: string;
  address: Address;
  status: MerchantStatus;
  supportedTransactionTypes: TransactionType[];
  supportedAcquirers: string[];
  supportedCardBrands: CardBrand[];
}

export interface MerchantFilters {
  page: number;
  size: number;
  sort?: string;
  status?: MerchantStatus;
}

export type MerchantStatus = "active" | "deactivated" | "hold";
