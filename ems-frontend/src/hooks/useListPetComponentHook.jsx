import { useState, useEffect } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import { listPets, deletePet, adoptPet, returnPetById } from "../services/PetService";
import { getStudentById } from "../services/StudentService";
import { toast } from "react-toastify";

const useListPetComponentHook = () => {
  const [pets, setPets] = useState([]);
  const [adoptStudent, setAdoptStudent] = useState(null);
  const [searchParams] = useSearchParams();
  const studentId = searchParams.get("studentId");
  const navigate = useNavigate();

  const getPets = async () => {
    try {
      const response = await listPets();
      setPets(response.data);
    } catch (err) {
      console.log(err);
    }
  };

  const loadAdoptStudent = async () => {
    if (!studentId) {
      setAdoptStudent(null);
      return;
    }
    try {
      const response = await getStudentById(studentId);
      const student = response.data;
      if (student.petId) {
        toast.error("最多只能有一只宠物");
        navigate("/students");
        return;
      }
      setAdoptStudent(student);
    } catch (err) {
      navigate("/students");
    }
  };

  useEffect(() => {
    getPets();
    loadAdoptStudent();
  }, [studentId]);

  const editPet = (id) => {
    navigate(`/edit-pet/${id}`);
  };

  const removePet = async (id) => {
    await deletePet(id);
    toast.error("Pet deleted successfully!");
    getPets();
  };

  const adoptPetForStudent = async (pet) => {
    if (pet.adopted) {
      toast.error("宠物已经被领养");
      return;
    }
    if (adoptStudent?.petId) {
      toast.error("最多只能有一只宠物");
      return;
    }
    try {
      await adoptPet(pet.id, studentId);
      toast.success("领养成功!");
      navigate("/students");
    } catch (err) {
      const message = err.response?.data?.message || "领养失败";
      toast.error(message);
    }
  };

  const cancelAdopt = () => {
    navigate("/students");
  };

  const returnPet = async (pet) => {
    if (!pet.adopted) {
      toast.error("该宠物未被领养，无法归还");
      return;
    }
    try {
      await returnPetById(pet.id);
      toast.success("归还成功!");
      getPets();
    } catch (err) {
      const message = err.response?.data?.message || "归还失败";
      toast.error(message);
    }
  };

  return {
    pets,
    adoptStudent,
    isAdoptMode: Boolean(studentId),
    getPets,
    editPet,
    removePet,
    adoptPetForStudent,
    cancelAdopt,
    returnPet,
  };
};

export default useListPetComponentHook;
