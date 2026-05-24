import api from "./api";

export const listAdoptionRequestsPage = (page, size = 10, status) => {
  const params = { page, size };
  if (status) {
    params.status = status;
  }
  return api.get("/api/adoption-requests", { params });
};

export const approveAdoptionRequest = (id) =>
  api.put(`/api/adoption-requests/${id}/approve`);

export const denyAdoptionRequest = (id) =>
  api.put(`/api/adoption-requests/${id}/deny`);
