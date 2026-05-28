import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:8080",
  withCredentials: true,
  headers: {
    "Content-Type": "application/json;charset=UTF-8",
    Accept: "application/json;charset=UTF-8",
  },
});

api.interceptors.response.use(
  (response) => response,
  (error) => {
    const requestUrl = error.config?.url || "";
    const isAuthLogin = requestUrl.includes("/api/auth/login");
    const isOnLoginFlow = window.location.pathname.startsWith("/login");

    if (error.response?.status === 401 && !isAuthLogin && !isOnLoginFlow) {
      window.location.href = "/login";
    }

    return Promise.reject(error);
  }
);

export default api;
