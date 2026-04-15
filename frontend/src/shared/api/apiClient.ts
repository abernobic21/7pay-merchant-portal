import axios from "axios";
import { applyAuthInterceptor } from "../lib/authInterceptor";

const apiClient = axios.create({
  baseURL: import.meta.env.VITE_API_URL,
  withCredentials: true,
  headers: {
    "Content-Type": "application/json",
  },
});

if (import.meta.env.DEV) {
  apiClient.interceptors.request.use((config) => {
    console.log("→", config.method?.toUpperCase(), config.url);
    return config;
  });
}

applyAuthInterceptor(apiClient, () => {
  import("@/router").then(({ router }) => {
    router.navigate("/login", { replace: true });
  });
});

export default apiClient;
