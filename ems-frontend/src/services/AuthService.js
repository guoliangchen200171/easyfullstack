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

export const registerDepartment = (data) => {
  return api.post("/api/auth/register/department", data);
};

export const registerStudent = (data) => {
  return api.post("/api/auth/register/student", data);
};

export const changePassword = (currentPassword, newPassword) => {
  return api.put("/api/auth/change-password", { currentPassword, newPassword });
};

export const verifyPasswordForChange = (username, currentPassword) => {
  return api.post("/api/auth/verify-password-for-change", {
    username,
    currentPassword,
  });
};

export const studentChangePasswordPublic = (
  username,
  currentPassword,
  newPassword
) => {
  return api.post("/api/auth/student-change-password", {
    username,
    currentPassword,
    newPassword,
  });
};
