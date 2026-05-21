import { useState } from "react";
import { toast } from "react-toastify";
import {
  getDepartmentById,
  getStudentsByDepartmentId,
} from "../services/DepartmentService";

const useViewDepartmentComponentHook = () => {
  const [departmentIdInput, setDepartmentIdInput] = useState("");
  const [department, setDepartment] = useState(null);
  const [students, setStudents] = useState([]);
  const [loading, setLoading] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");

  const searchDepartment = async (e) => {
    e.preventDefault();
    if (!departmentIdInput.trim()) {
      toast.error("请输入部门 ID");
      return;
    }

    const id = departmentIdInput.trim();
    setLoading(true);
    setDepartment(null);
    setStudents([]);
    setErrorMessage("");

    try {
      const deptRes = await getDepartmentById(id);
      setDepartment(deptRes.data);

      try {
        const studentsRes = await getStudentsByDepartmentId(id);
        setStudents(studentsRes.data);
      } catch {
        setStudents([]);
      }
    } catch (error) {
      setDepartment(null);
      setStudents([]);
      if (!error.response) {
        toast.error("无法连接服务器，请稍后重试");
      } else {
        setErrorMessage(`查找的部门 ID（${id}）不存在`);
      }
    } finally {
      setLoading(false);
    }
  };

  return {
    departmentIdInput,
    setDepartmentIdInput,
    department,
    students,
    loading,
    errorMessage,
    searchDepartment,
  };
};

export default useViewDepartmentComponentHook;
