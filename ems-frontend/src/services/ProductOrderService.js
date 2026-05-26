import api from "./api";

const REST_API_URL = "/api/product-orders";

export const listProductOrdersPage = (
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

  return api.get(REST_API_URL, { params });
};
