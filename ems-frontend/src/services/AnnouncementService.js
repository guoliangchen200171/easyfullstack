import api from "./api";

const REST_API_URL = "/api/announcements";

export const listAnnouncements = () => api.get(REST_API_URL);

export const listActiveAnnouncements = () => api.get(`${REST_API_URL}/active`);

export const createAnnouncement = (announcement) =>
  api.post(REST_API_URL, announcement);

export const getAnnouncementById = (id) => api.get(`${REST_API_URL}/${id}`);

export const updateAnnouncement = (id, announcement) =>
  api.put(`${REST_API_URL}/${id}`, announcement);

export const deleteAnnouncement = (id) => api.delete(`${REST_API_URL}/${id}`);
