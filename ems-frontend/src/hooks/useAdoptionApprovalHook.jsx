import { useState, useEffect, useCallback } from "react";
import { toast } from "react-toastify";
import {
  listAdoptionRequestsPage,
  approveAdoptionRequest,
  denyAdoptionRequest,
} from "../services/AdoptionRequestService";

const PAGE_SIZE = 10;

const formatDateTime = (value) => {
  if (!value) {
    return "—";
  }
  return new Date(value).toLocaleString();
};

const statusLabel = (status) => {
  switch (status) {
    case "PENDING":
      return "待审批";
    case "APPROVED":
      return "已批准";
    case "DENIED":
      return "已拒绝";
    default:
      return status;
  }
};

const useAdoptionApprovalHook = () => {
  const [requests, setRequests] = useState([]);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);
  const [statusFilter, setStatusFilter] = useState("PENDING");

  const fetchRequests = useCallback(
    async (pageToLoad = page, status = statusFilter) => {
      try {
        const response = await listAdoptionRequestsPage(
          pageToLoad,
          PAGE_SIZE,
          status || undefined,
        );
        const data = response.data;
        setRequests(data.content);
        setTotalPages(data.totalPages);
        setTotalElements(data.totalElements);
      } catch (err) {
        console.log(err);
      }
    },
    [page, statusFilter],
  );

  useEffect(() => {
    fetchRequests(page, statusFilter);
  }, [page, statusFilter, fetchRequests]);

  const handlePageChange = (newPage) => {
    setPage(newPage);
  };

  const handleStatusFilterChange = (status) => {
    setStatusFilter(status);
    setPage(0);
  };

  const handleApprove = async (id) => {
    try {
      await approveAdoptionRequest(id);
      toast.success("已批准领养申请");
      fetchRequests(page, statusFilter);
    } catch (err) {
      toast.error(err.response?.data?.message || "批准失败");
    }
  };

  const handleDeny = async (id) => {
    try {
      await denyAdoptionRequest(id);
      toast.success("已拒绝领养申请");
      fetchRequests(page, statusFilter);
    } catch (err) {
      toast.error(err.response?.data?.message || "拒绝失败");
    }
  };

  return {
    requests,
    page,
    totalPages,
    totalElements,
    statusFilter,
    formatDateTime,
    statusLabel,
    handlePageChange,
    handleStatusFilterChange,
    handleApprove,
    handleDeny,
  };
};

export default useAdoptionApprovalHook;
