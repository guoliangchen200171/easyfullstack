import { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { toast } from "react-toastify";
import { getPetById, createPet, updatePet } from "../services/PetService";
import { listStudents } from "../services/StudentService";

const usePetComponentHook = () => {
  const [name, setName] = useState("");
  const [description, setDescription] = useState("");
  const [age, setAge] = useState("");
  const [category, setCategory] = useState("");
  const [adopted, setAdopted] = useState(false);
  const [studentId, setStudentId] = useState("");
  const [students, setStudents] = useState([]);
  const [title, setTitle] = useState("");
  const { id } = useParams();
  const navigate = useNavigate();

  const loadStudents = async () => {
    const response = await listStudents();
    setStudents(response.data);
  };

  const loadPet = async (petId) => {
    const response = await getPetById(petId);
    const pet = response.data;
    setName(pet.name);
    setDescription(pet.description);
    setAge(pet.age ?? "");
    setCategory(pet.category ?? "");
    setAdopted(pet.adopted ?? false);
    setStudentId(pet.studentId ?? "");
  };

  useEffect(() => {
    loadStudents();
    if (id) {
      setTitle("Update Pet");
      loadPet(id);
    } else {
      setTitle("Add Pet");
      setAdopted(false);
      setStudentId("");
    }
  }, [id]);

  const saveOrUpdatePet = async (e) => {
    e.preventDefault();

    if (id && adopted && !studentId) {
      toast.error("Please select a student when pet is adopted!");
      return;
    }

    const pet = {
      name,
      description,
      age: age === "" ? null : Number(age),
      category,
      adopted: id ? adopted : false,
      studentId: id && adopted ? Number(studentId) : null,
    };

    if (name && category) {
      if (id) {
        await updatePet(id, pet);
        toast.info("Pet updated successfully!");
        navigate("/pets");
        return;
      }
      await createPet(pet);
      toast.success("Pet added successfully!");
      navigate("/pets");
    } else {
      toast.error("Please fill in the pet name and category!");
    }
  };

  return {
    name,
    setName,
    description,
    setDescription,
    age,
    setAge,
    category,
    setCategory,
    adopted,
    setAdopted,
    studentId,
    setStudentId,
    students,
    title,
    saveOrUpdatePet,
    id,
  };
};

export default usePetComponentHook;
