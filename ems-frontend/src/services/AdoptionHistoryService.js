import api from "./api";

const REST_API_URL = "/api/adoption-history";

export const listAdoptionHistory = () => {
  return api.get(REST_API_URL);
};

export const listAdoptionHistoryPage = (page = 0, size = 10) => {
  return api.get(`${REST_API_URL}?page=${page}&size=${size}`);
};

export const listAdoptionHistoryByStudent = (studentId) => {
  return api.get(`${REST_API_URL}/student/${studentId}`);
};
