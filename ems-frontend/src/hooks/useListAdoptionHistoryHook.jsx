import { useState, useEffect, useCallback } from "react";
import { listAdoptionHistoryPage } from "../services/AdoptionHistoryService";

const PAGE_SIZE = 10;

const formatDateTime = (value) => {
  if (!value) {
    return "—";
  }
  return new Date(value).toLocaleString();
};

const useListAdoptionHistoryHook = () => {
  const [history, setHistory] = useState([]);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);

  const fetchHistory = useCallback(async (pageToLoad = page) => {
    try {
      const response = await listAdoptionHistoryPage(pageToLoad, PAGE_SIZE);
      const data = response.data;
      setHistory(data.content);
      setTotalPages(data.totalPages);
      setTotalElements(data.totalElements);
    } catch (err) {
      console.log(err);
    }
  }, [page]);

  useEffect(() => {
    fetchHistory(page);
  }, [page, fetchHistory]);

  const handlePageChange = (newPage) => {
    setPage(newPage);
  };

  return {
    history,
    page,
    totalPages,
    totalElements,
    formatDateTime,
    handlePageChange,
  };
};

export default useListAdoptionHistoryHook;
