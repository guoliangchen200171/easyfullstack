import api from "./api";

const REST_API_URL = "/api/adoption-history";

export const listAdoptionHistory = () => {
  return api.get(REST_API_URL);
};

export const listAdoptionHistoryByStudent = (studentId) => {
  return api.get(`${REST_API_URL}/student/${studentId}`);
};
