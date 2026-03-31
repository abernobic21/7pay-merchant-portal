import type { Address } from "@/shared/types";

export interface Merchant {
  shortName: string;
  fullName: string;
  oib: string;
  address: Address;
  status: string;
}
