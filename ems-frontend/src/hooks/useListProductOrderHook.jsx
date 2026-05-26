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
  const [sort, setSort] = useState("desc");
  const [searchInput, setSearchInput] = useState("");
  const [fromDateInput, setFromDateInput] = useState("");
  const [toDateInput, setToDateInput] = useState("");
  const [minPriceInput, setMinPriceInput] = useState("");
  const [maxPriceInput, setMaxPriceInput] = useState("");
  const [appliedName, setAppliedName] = useState("");
  const [appliedFrom, setAppliedFrom] = useState("");
  const [appliedTo, setAppliedTo] = useState("");
  const [appliedMinPrice, setAppliedMinPrice] = useState("");
  const [appliedMaxPrice, setAppliedMaxPrice] = useState("");
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);

  const hasAppliedFilters =
    appliedName ||
    appliedFrom ||
    appliedTo ||
    appliedMinPrice !== "" ||
    appliedMaxPrice !== "";

  const fetchOrders = useCallback(async () => {
    try {
      const response = await listProductOrdersPage(page, PAGE_SIZE, sort, {
        name: appliedName,
        from: appliedFrom || undefined,
        to: appliedTo || undefined,
        minPrice: appliedMinPrice === "" ? undefined : appliedMinPrice,
        maxPrice: appliedMaxPrice === "" ? undefined : appliedMaxPrice,
      });
      const data = response.data;
      setOrders(data.content);
      setTotalPages(data.totalPages);
      setTotalElements(data.totalElements);
    } catch (err) {
      console.log(err);
    }
  }, [
    page,
    sort,
    appliedName,
    appliedFrom,
    appliedTo,
    appliedMinPrice,
    appliedMaxPrice,
  ]);

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

  const handleSearchSubmit = (e) => {
    e.preventDefault();
    setAppliedName(searchInput.trim());
    setAppliedFrom(fromDateInput);
    setAppliedTo(toDateInput);
    setAppliedMinPrice(minPriceInput);
    setAppliedMaxPrice(maxPriceInput);
    setPage(0);
  };

  const handleClearSearch = () => {
    setSearchInput("");
    setFromDateInput("");
    setToDateInput("");
    setMinPriceInput("");
    setMaxPriceInput("");
    setAppliedName("");
    setAppliedFrom("");
    setAppliedTo("");
    setAppliedMinPrice("");
    setAppliedMaxPrice("");
    setPage(0);
  };

  return {
    orders,
    page,
    sort,
    searchInput,
    fromDateInput,
    toDateInput,
    minPriceInput,
    maxPriceInput,
    hasAppliedFilters,
    totalPages,
    totalElements,
    formatDateTime,
    formatMoney,
    handlePageChange,
    handleSortChange,
    handleSearchSubmit,
    handleClearSearch,
    setSearchInput,
    setFromDateInput,
    setToDateInput,
    setMinPriceInput,
    setMaxPriceInput,
  };
};

export default useListProductOrderHook;
