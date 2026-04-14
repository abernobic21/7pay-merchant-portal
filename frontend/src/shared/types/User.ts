export interface User {
  id: string;
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  role: "admin" | "user";
  accessLevel: "merchant" | "branch";
  merchantId: string | null;
  status: "active" | "deactivated";
  firstLoginFlag: boolean;
  createdAt: string;
}
