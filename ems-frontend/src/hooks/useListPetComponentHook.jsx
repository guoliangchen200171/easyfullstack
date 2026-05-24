import { useState, useEffect } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import { listPets, deletePet, adoptPet, returnPetById, searchPetsByName as searchPetsByNameApi } from "../services/PetService";
import { getStudentById } from "../services/StudentService";
import { toast } from "react-toastify";

const useListPetComponentHook = () => {
  const [pets, setPets] = useState([]);
  const [searchName, setSearchName] = useState("");
  const [isSearching, setIsSearching] = useState(false);
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
    if (isSearching) {
      searchPets();
    } else {
      getPets();
    }
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
      if (isSearching) {
        searchPets();
      } else {
        getPets();
      }
    } catch (err) {
      const message = err.response?.data?.message || "归还失败";
      toast.error(message);
    }
  };

  const searchPets = async (e) => {
    e?.preventDefault();
    if (!searchName.trim()) {
      toast.error("请输入宠物名称");
      return;
    }
    try {
      const response = await searchPetsByNameApi(searchName.trim());
      setPets(response.data);
      setIsSearching(true);
      if (response.data.length === 0) {
        toast.info("未找到匹配的宠物");
      }
    } catch (err) {
      const message = err.response?.data?.message || "查询失败";
      toast.error(message);
    }
  };

  const resetSearch = () => {
    setSearchName("");
    setIsSearching(false);
    getPets();
  };

  return {
    pets,
    searchName,
    setSearchName,
    isSearching,
    adoptStudent,
    isAdoptMode: Boolean(studentId),
    getPets,
    editPet,
    removePet,
    adoptPetForStudent,
    cancelAdopt,
    returnPet,
    searchPets,
    resetSearch,
  };
};

export default useListPetComponentHook;
