import { useState, useEffect, useCallback } from "react";
import { listProductOrdersPage } from "../services/ProductOrderService";

const PAGE_SIZE = 10;

const parseLocalDateTime = (value) => {
  if (!value) {
    return null;
  }
  if (Array.isArray(value)) {
    const [year, month, day, hour = 0, minute = 0, second = 0] = value;
    return new Date(year, month - 1, day, hour, minute, second);
  }
  const normalized =
    typeof value === "string" && value.includes(" ") && !value.includes("T")
      ? value.replace(" ", "T")
      : value;
  const date = new Date(normalized);
  return Number.isNaN(date.getTime()) ? null : date;
};

const formatDateTime = (value) => {
  const date = parseLocalDateTime(value);
  if (!date) {
    return "—";
  }
  return date.toLocaleString();
};

const formatMoney = (value) => {
  const amount = Number(value ?? 0);
  return Number.isNaN(amount) ? "0.00" : amount.toFixed(2);
};

const useListProductOrderHook = () => {
  const [orders, setOrders] = useState([]);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);

  const fetchOrders = useCallback(async (pageToLoad = page) => {
    try {
      const response = await listProductOrdersPage(pageToLoad, PAGE_SIZE);
      const data = response.data;
      setOrders(data.content);
      setTotalPages(data.totalPages);
      setTotalElements(data.totalElements);
    } catch (err) {
      console.log(err);
    }
  }, [page]);

  useEffect(() => {
    fetchOrders(page);
  }, [page, fetchOrders]);

  const handlePageChange = (newPage) => {
    setPage(newPage);
  };

  return {
    orders,
    page,
    totalPages,
    totalElements,
    formatDateTime,
    formatMoney,
    handlePageChange,
  };
};

export default useListProductOrderHook;
