import axios from "axios";

const REST_API_URL = "http://localhost:8080/api/adoption-history";

export const listAdoptionHistory = () => {
  return axios.get(REST_API_URL);
};

export const listAdoptionHistoryByStudent = (studentId) => {
  return axios.get(`${REST_API_URL}/student/${studentId}`);
};
