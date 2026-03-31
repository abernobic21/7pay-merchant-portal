import axios from "axios";

const apiClient = axios.create({
  baseURL: import.meta.env.VITE_API_URL,
  withCredentials: true,
});

apiClient.interceptors.request.use((config) => {
  console.log(
    "API call:",
    config.method?.toUpperCase(),
    (config.baseURL ?? "") + config.url,
  );
  return config;
});

export default apiClient;
