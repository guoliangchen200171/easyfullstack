import api from "./api";

export const getMyProfile = () => api.get("/api/students/me");

export const adoptPetForMe = (petId) =>
  api.put(`/api/students/me/adopt-pet/${petId}`);

export const getMyPendingAdoptionRequest = () =>
  api.get("/api/students/me/adoption-request/pending");

export const returnPetForMe = () => api.put("/api/students/me/return-pet");

export const getMyHistory = () => api.get("/api/students/me/history");

export const listProductsForMe = () => api.get("/api/students/me/products");

export const purchaseProductForMe = (productId, quantity) =>
  api.post(`/api/students/me/products/${productId}/purchase`, { quantity });

export const listMyProductOrdersPage = (
  page = 0,
  size = 10,
  sort = "desc",
  filters = {}
) => {
  const { name, from, to, minPrice, maxPrice } = filters;
  const params = { page, size, sort };

  if (name?.trim()) {
    params.name = name.trim();
  }
  if (from) {
    params.from = from;
  }
  if (to) {
    params.to = to;
  }
  if (minPrice !== "" && minPrice != null) {
    params.minPrice = minPrice;
  }
  if (maxPrice !== "" && maxPrice != null) {
    params.maxPrice = maxPrice;
  }

  return api.get("/api/students/me/product-orders", { params });
};
