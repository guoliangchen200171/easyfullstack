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

  const searchDepartment = async (e) => {
    e.preventDefault();
    if (!departmentIdInput.trim()) {
      toast.error("请输入部门 ID");
      return;
    }

    setLoading(true);
    setDepartment(null);
    setStudents([]);

    try {
      const [deptRes, studentsRes] = await Promise.all([
        getDepartmentById(departmentIdInput.trim()),
        getStudentsByDepartmentId(departmentIdInput.trim()),
      ]);
      setDepartment(deptRes.data);
      setStudents(studentsRes.data);
    } catch (error) {
      if (error.response?.status === 404) {
        toast.error("未找到该部门，请检查 ID 是否正确");
      } else {
        toast.error("查询失败，请确认后端服务已启动");
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
    searchDepartment,
  };
};

export default useViewDepartmentComponentHook;
