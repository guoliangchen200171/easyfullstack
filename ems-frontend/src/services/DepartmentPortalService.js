import api from "./api";

export const getMyDepartment = () => api.get("/api/department/me");

export const getMyDepartmentStudents = () =>
  api.get("/api/department/me/students");
