import api from "./api";

export const getMyProfile = () => api.get("/api/students/me");

export const adoptPetForMe = (petId) =>
  api.put(`/api/students/me/adopt-pet/${petId}`);

export const getMyPendingAdoptionRequest = () =>
  api.get("/api/students/me/adoption-request/pending");

export const returnPetForMe = () => api.put("/api/students/me/return-pet");

export const getMyHistory = () => api.get("/api/students/me/history");
