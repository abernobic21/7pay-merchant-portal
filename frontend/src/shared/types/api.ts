export interface Page {
  size: number;
  number: number;
  totalElements: number;
  totalPages: number;
}

export interface ApiResponse<T> {
  content: T[];
  page: Page;
}
