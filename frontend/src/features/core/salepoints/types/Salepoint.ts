import type { Address } from "@/shared/types";
import type {
  CardBrand,
  TransactionType,
} from "../../transactions/types/Transaction";

export interface Salepoint {
  name: string;
  mid: string;
  address: Address;
  status: SalepointStatus;
  merchantOib: string;
}

export interface SalepointFilters {
  page: number;
  size: number;
  sort?: string;
  merchantOib?: string;
}

export interface SalepointDetail {
  name: string;
  mid: string;
  address: Address;
  status: SalepointStatus;
  merchantOib: string;
  acquirerMerchantData: AcquirerMerchantData[];
}

export interface AcquirerMerchantData {
  acquirerMid: string;
  acquirerHost: string;
  transactionTypes: TransactionType[];
  cardBrands: CardBrand[];
}

export type SalepointStatus = "active" | "deactivated" | "hold";
