import { useState, useEffect, useCallback } from "react";
import { useNavigate } from "react-router-dom";
import { listDepartmentsPage, deleteDepartment } from "../services/DepartmentService";
import { toast } from "react-toastify";

const PAGE_SIZE = 10;

const useListDepartmentComponentHook = () => {
  const [departments, setDepartments] = useState([]);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);
  const navigate = useNavigate();

  const getDepartments = useCallback(async (pageToLoad = page) => {
    try {
      const response = await listDepartmentsPage(pageToLoad, PAGE_SIZE);
      const data = response.data;
      if (data.content.length === 0 && pageToLoad > 0 && data.totalPages > 0) {
        setPage(pageToLoad - 1);
        return;
      }
      setDepartments(data.content);
      setTotalPages(data.totalPages);
      setTotalElements(data.totalElements);
    } catch (err) {
      console.log(err);
    }
  }, [page]);

  const updateDepartment = (id) => {
    navigate(`/edit-department/${id}`);
  };

  const removeDepartment = async (id) => {
    await deleteDepartment(id);
    toast.error("Department deleted successfully!");
    getDepartments(page);
  };

  useEffect(() => {
    getDepartments(page);
  }, [page, getDepartments]);

  const handlePageChange = (newPage) => {
    setPage(newPage);
  };

  return {
    departments,
    page,
    totalPages,
    totalElements,
    updateDepartment,
    removeDepartment,
    handlePageChange,
  };
};

export default useListDepartmentComponentHook;
