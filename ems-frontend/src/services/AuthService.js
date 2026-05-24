import api from "./api";

export const login = (username, password) => {
  return api.post("/api/auth/login", { username, password });
};

export const logout = () => {
  return api.post("/api/auth/logout");
};

export const checkAuth = () => {
  return api.get("/api/auth/me");
};
