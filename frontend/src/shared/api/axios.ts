import axios from "axios";
import { applyAuthInterceptor } from "../lib/authInterceptor";

const apiClient = axios.create({
  baseURL: import.meta.env.VITE_API_URL,
  withCredentials: true,
  headers: {
    "Content-Type": "application/json",
  },
});

applyAuthInterceptor(apiClient);

export default apiClient;
