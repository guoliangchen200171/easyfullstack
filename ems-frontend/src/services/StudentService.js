import api from "./api";

const REST_API_URL = "/api/students";

export const listStudents = () => {
  return api.get(REST_API_URL);
};

export const createStudent = (student) => {
  return api.post(REST_API_URL, student);
};

export const deleteStudent = (id) => {
  return api.delete(`${REST_API_URL}/${id}`);
};

export const getStudentById = (id) => {
  return api.get(`${REST_API_URL}/${id}`);
};

export const updateStudent = (id, student) => {
  return api.put(`${REST_API_URL}/${id}`, student);
};

export const returnPet = (studentId) => {
  return api.put(`${REST_API_URL}/${studentId}/return-pet`);
};

export const resetReturnCount = (studentId) => {
  return api.put(`${REST_API_URL}/${studentId}/reset-return-count`);
};
