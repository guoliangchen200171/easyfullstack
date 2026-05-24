import api from "./api";

export const getMyProfile = () => api.get("/api/students/me");

export const adoptPetForMe = (petId) =>
  api.put(`/api/students/me/adopt-pet/${petId}`);

export const returnPetForMe = () => api.put("/api/students/me/return-pet");

export const getMyHistory = () => api.get("/api/students/me/history");
