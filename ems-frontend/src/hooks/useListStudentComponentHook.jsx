import { useState, useEffect, useCallback } from "react";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import {
  listStudentsPage,
  deleteStudent,
  returnPet,
  resetReturnCount,
  addStudentDeposit,
} from "../services/StudentService";
import { listDepartments } from "../services/DepartmentService";

const PAGE_SIZE = 10;

const useListStudentComponentHook = () => {
  const [students, setStudents] = useState([]);
  const [departments, setDepartments] = useState([]);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [totalElements, setTotalElements] = useState(0);
  const [depositEmail, setDepositEmail] = useState("");
  const [depositAmount, setDepositAmount] = useState("");
  const navigate = useNavigate();

  const fetchStudents = useCallback(async (pageToLoad = page) => {
    try {
      const response = await listStudentsPage(pageToLoad, PAGE_SIZE);
      const data = response.data;
      if (data.content.length === 0 && pageToLoad > 0 && data.totalPages > 0) {
        setPage(pageToLoad - 1);
        return;
      }
      setStudents(data.content);
      setTotalPages(data.totalPages);
      setTotalElements(data.totalElements);
    } catch (err) {
      console.log(err);
    }
  }, [page]);

  const fetchDepartments = async () => {
    const response = await listDepartments();
    setDepartments(response.data);
  };

  useEffect(() => {
    fetchStudents(page);
  }, [page, fetchStudents]);

  useEffect(() => {
    fetchDepartments();
  }, []);

  const getDepartmentName = (departmentId) => {
    const department = departments.find((dept) => dept.id === departmentId);
    return department ? department.departmentName : "Unknown Department";
  };

  const updateStudent = (id) => {
    navigate(`/edit-student/${id}`);
  };

  const adoptPetForStudent = (student) => {
    if (student.petId) {
      toast.error("最多只能有一只宠物");
      return;
    }
    navigate(`/pets?studentId=${student.id}`);
  };

  const returnPetForStudent = async (student) => {
    if (!student.petId) {
      toast.error("该学生没有宠物");
      return;
    }
    try {
      await returnPet(student.id);
      toast.success("取消领养成功!");
      fetchStudents(page);
    } catch (err) {
      const message = err.response?.data?.message || "取消领养失败";
      toast.error(message);
    }
  };

  const deleteStudentById = async (id) => {
    await deleteStudent(id);
    toast.error("Student deleted successfully!");
    fetchStudents(page);
  };

  const resetReturnCountForStudent = async (student) => {
    if ((student.returnCount ?? 0) === 0) {
      toast.info("该学生归还次数已为 0");
      return;
    }
    try {
      await resetReturnCount(student.id);
      toast.success("归还次数已重置");
      fetchStudents(page);
    } catch (err) {
      toast.error(err.response?.data?.message || "重置失败");
    }
  };

  const handlePageChange = (newPage) => {
    setPage(newPage);
  };

  const parseDepositInputs = () => {
    const email = depositEmail.trim();
    const amount = Number(depositAmount);
    if (!email) {
      toast.error("请输入学生邮箱");
      return null;
    }
    if (Number.isNaN(amount) || amount <= 0) {
      toast.error("金额必须大于 0");
      return null;
    }
    return { email, amount };
  };

  const handleAddDeposit = async () => {
    const inputs = parseDepositInputs();
    if (!inputs) {
      return;
    }
    try {
      await addStudentDeposit(inputs.email, inputs.amount);
      toast.success("存款已增加");
      fetchStudents(page);
    } catch (err) {
      toast.error(err.response?.data?.message || "加存款失败");
    }
  };

  return {
    students,
    page,
    totalPages,
    totalElements,
    depositEmail,
    setDepositEmail,
    depositAmount,
    setDepositAmount,
    getDepartmentName,
    updateStudent,
    adoptPetForStudent,
    returnPetForStudent,
    deleteStudentById,
    resetReturnCountForStudent,
    handleAddDeposit,
    handlePageChange,
  };
};

export default useListStudentComponentHook;
