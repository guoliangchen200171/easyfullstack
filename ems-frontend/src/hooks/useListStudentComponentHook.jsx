import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import { listStudents, deleteStudent } from "../services/StudentService";
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
    deleteStudentById,
  };
};

export default useListStudentComponentHook;
