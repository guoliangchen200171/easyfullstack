import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import { listStudents, deleteStudent, returnPet } from "../services/StudentService";
import { listDepartments } from "../services/DepartmentService";

const useListStudentComponentHook = () => {
  const [students, setStudents] = useState([]);
  const [departments, setDepartments] = useState([]);
  const navigate = useNavigate();

  const fetchStudents = async () => {
    try {
      const response = await listStudents();
      setStudents(response.data);
    } catch (err) {
      console.log(err);
    }
  };

  const fetchDepartments = async () => {
    const response = await listDepartments();
    setDepartments(response.data);
  };

  useEffect(() => {
    fetchStudents();
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
      fetchStudents();
    } catch (err) {
      const message = err.response?.data?.message || "取消领养失败";
      toast.error(message);
    }
  };

  const deleteStudentById = async (id) => {
    await deleteStudent(id);
    toast.error("Student deleted successfully!");
    fetchStudents();
  };

  return {
    students,
    departments,
    fetchStudents,
    fetchDepartments,
    getDepartmentName,
    updateStudent,
    adoptPetForStudent,
    returnPetForStudent,
    deleteStudentById,
  };
};

export default useListStudentComponentHook;
