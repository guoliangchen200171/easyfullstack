import { useState, useEffect, useCallback } from "react";
import { getRestockRecordsPage } from "../services/RestockRecordService";

const PAGE_SIZE = 10;

const formatDateTime = (value) => {
  if (!value) return "—";
  return new Date(value).toLocaleString();
};

const useListRestockRecordHook = () => {
  const [records, setRecords] = useState([]);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);

  const fetchRecords = useCallback(async (pageToLoad = page) => {
    try {
      const response = await getRestockRecordsPage(pageToLoad, PAGE_SIZE);
      const data = response.data;
      setRecords(data.content);
      setTotalPages(data.totalPages);
      setTotalElements(data.totalElements);
    } catch (err) {
      console.log(err);
    }
  }, [page]);

  useEffect(() => {
    fetchRecords(page);
  }, [page, fetchRecords]);

  const handlePageChange = (newPage) => {
    setPage(newPage);
  };

  return {
    records,
    page,
    totalPages,
    totalElements,
    formatDateTime,
    handlePageChange,
  };
};

export default useListRestockRecordHook;
