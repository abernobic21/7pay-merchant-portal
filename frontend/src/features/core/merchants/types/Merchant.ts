import type { Address } from "./Address";

export interface Merchant {
  shortName: string;
  fullName: string;
  oib: string;
  oibExtension: string | null;
  taxNumber: string | null;
  address: Address;
  status: string;
}
