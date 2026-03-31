import type { Address } from "@/shared/types";

export interface Salepoint {
  name: string;
  mid: string;
  address: Address;
  status: string;
  merchantOib: string;
}
