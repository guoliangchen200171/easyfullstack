import { useState, useEffect } from "react";
import { listAdoptionHistory } from "../services/AdoptionHistoryService";

const formatDateTime = (value) => {
  if (!value) {
    return "—";
  }
  return new Date(value).toLocaleString();
};

const useListAdoptionHistoryHook = () => {
  const [history, setHistory] = useState([]);

  const fetchHistory = async () => {
    try {
      const response = await listAdoptionHistory();
      setHistory(response.data);
    } catch (err) {
      console.log(err);
    }
  };

  useEffect(() => {
    fetchHistory();
  }, []);

  return {
    history,
    formatDateTime,
  };
};

export default useListAdoptionHistoryHook;
