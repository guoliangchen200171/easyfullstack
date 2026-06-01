import api from "./api";

const REST_API_URL = "/api/products";

export const listProducts = () => api.get(REST_API_URL);

export const createProduct = (product) => api.post(REST_API_URL, product);

export const getProductById = (id) => api.get(`${REST_API_URL}/${id}`);

export const updateProduct = (id, product) =>
  api.put(`${REST_API_URL}/${id}`, product);

export const addProductStock = (id, quantity) =>
  api.put(`${REST_API_URL}/${id}/stock/add`, { quantity });

export const deductProductStock = (id, quantity) =>
  api.put(`${REST_API_URL}/${id}/stock/deduct`, { quantity });

export const addProductStockByName = (name, quantity) =>
  api.put(`${REST_API_URL}/stock/add-by-name`, { name, quantity });

export const deductProductStockByName = (name, quantity) =>
  api.put(`${REST_API_URL}/stock/deduct-by-name`, { name, quantity });

export const deleteProduct = (id) => api.delete(`${REST_API_URL}/${id}`);
