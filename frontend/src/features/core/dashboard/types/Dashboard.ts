export interface DashboardStats {
  totalRevenue: number;
  totalTransactions: number;
  avgTransactionValue: number;
  revenueByCardBrand: RevenueByCardBrand[];
  revenueByAcquirer: RevenueByAcquirer[];
  cachedAt: string;
}

export interface RevenueByCardBrand {
  cardBrand: string;
  revenue: number;
  count: number;
}

export interface RevenueByAcquirer {
  acquirerHost: string;
  revenue: number;
  count: number;
}

export interface DashboardFilters {
  timeFrom: string;
  timeUntil: string;
  mid?: string;
  tid?: string;
}
