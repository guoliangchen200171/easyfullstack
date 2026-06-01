import api from "./api";

const REST_API_URL = "/api/announcement-history";

export const getAnnouncementHistoryPage = (page = 0, size = 10) => {
  return api.get(`${REST_API_URL}?page=${page}&size=${size}`);
};
