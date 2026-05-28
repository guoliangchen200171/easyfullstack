import api from "./api";

const REST_API_URL = "/api/membership-levels";

export const listMembershipLevels = () => api.get(REST_API_URL);

export const getMembershipLevelById = (id) => api.get(`${REST_API_URL}/${id}`);

export const createMembershipLevel = (level) => api.post(REST_API_URL, level);

export const updateMembershipLevel = (id, level) =>
  api.put(`${REST_API_URL}/${id}`, level);

export const deleteMembershipLevel = (id) => api.delete(`${REST_API_URL}/${id}`);
