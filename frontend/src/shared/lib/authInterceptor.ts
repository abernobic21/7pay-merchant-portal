import type { AxiosInstance } from "axios";
import { router } from "@/router";
import { ROUTES } from "@/router/routes";

export const applyAuthInterceptor = (client: AxiosInstance) => {
  client.interceptors.request.use((config) => {
    if (import.meta.env.DEV) {
      console.log(
        "API call:",
        config.method?.toUpperCase(),
        (config.baseURL ?? "") + config.url,
      );
    }
    return config;
  });

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
      const isRefreshEndpoint = original?.url?.includes("/auth/refresh");
      const alreadyRetried = original?._retry;

      if (is401 && isRefreshEndpoint) {
        return Promise.reject(error);
      }

      if (is401 && !alreadyRetried) {
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
          await client.post("/api/v1/auth/refresh");
          processQueue(null);
          return client(original);
        } catch (refreshError) {
          processQueue(refreshError);
          router.navigate(ROUTES.LOGIN, { replace: true });
          return Promise.reject(refreshError);
        } finally {
          isRefreshing = false;
        }
      }

      const message =
        error.response?.data?.message ?? error.message ?? "Unknown error";

      return Promise.reject({ ...error, message });
    },
  );
};
