import type { AxiosInstance } from "axios";

const PUBLIC_ENDPOINTS = [
  "auth/login",
  "auth/refresh",
  "auth/set-password",
  "auth/reset-password",
  "auth/2fa/validate",
  "auth/2fa/validate-backup",
];

const isPublicEndpoint = (url?: string) =>
  PUBLIC_ENDPOINTS.some((endpoint) => url?.includes(endpoint));

export const applyAuthInterceptor = (
  client: AxiosInstance,
  onSessionExpired: () => void,
) => {
  let isRefreshing = false;
  let queue: Array<{
    resolve: (value?: unknown) => void;
    reject: (reason?: unknown) => void;
  }> = [];

  const processQueue = (error: unknown) => {
    queue.forEach((p) => (error ? p.reject(error) : p.resolve()));
    queue = [];
  };

  client.interceptors.response.use(
    (response) => response,
    async (error) => {
      const original = error.config;
      const is401 = error.response?.status === 401;
      const alreadyRetried = original?._retry;

      if (!is401 || alreadyRetried || isPublicEndpoint(original?.url)) {
        return Promise.reject(error);
      }

      original._retry = true;

      if (isRefreshing) {
        return new Promise((resolve, reject) => {
          queue.push({ resolve, reject });
        })
          .then(() => client(original))
          .catch((err) => Promise.reject(err));
      }

      isRefreshing = true;

      try {
        await client.post("/auth/refresh");
        processQueue(null);
        return client(original);
      } catch (refreshError) {
        processQueue(refreshError);
        onSessionExpired();
        return Promise.reject(refreshError);
      } finally {
        isRefreshing = false;
      }
    },
  );
};
