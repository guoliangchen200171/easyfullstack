import { useState, useEffect, useCallback } from "react";
import { listMyProductOrdersPage } from "../services/StudentPortalService";

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

const useStudentProductOrderHistoryHook = () => {
  const [orders, setOrders] = useState([]);
  const [page, setPage] = useState(0);
  const [sort, setSort] = useState("desc");
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);

  const fetchOrders = useCallback(async () => {
    try {
      const response = await listMyProductOrdersPage(page, PAGE_SIZE, sort);
      const data = response.data;
      setOrders(data.content);
      setTotalPages(data.totalPages);
      setTotalElements(data.totalElements);
    } catch (err) {
      console.log(err);
    }
  }, [page, sort]);

  useEffect(() => {
    fetchOrders();
  }, [fetchOrders]);

  const handlePageChange = (newPage) => {
    setPage(newPage);
  };

  const handleSortChange = (newSort) => {
    setSort(newSort);
    setPage(0);
  };

  return {
    orders,
    page,
    sort,
    totalPages,
    totalElements,
    formatDateTime,
    formatMoney,
    handlePageChange,
    handleSortChange,
  };
};

export default useStudentProductOrderHistoryHook;
