import api from "./api";

const REST_API_URL = "/api/departments";

export const listDepartments = () => {
  return api.get(REST_API_URL);
};

export const listDepartmentsPage = (page = 0, size = 10) => {
  return api.get(`${REST_API_URL}?page=${page}&size=${size}`);
};

export const createDepartment = (department) => {
  return api.post(REST_API_URL, department);
};

export const getDepartmentById = (id) => {
  return api.get(REST_API_URL + "/" + id);
};

export const updateDeparment = (id, department) => {
  return api.put(REST_API_URL + "/" + id, department);
};

export const deleteDepartment = (id) => {
  return api.delete(REST_API_URL + "/" + id);
};

export const getStudentsByDepartmentId = (id) => {
  return api.get(REST_API_URL + "/" + id + "/students");
};
