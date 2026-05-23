import { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { toast } from "react-toastify";
import { getPetById, createPet, updatePet } from "../services/PetService";

const usePetComponentHook = () => {
  const [name, setName] = useState("");
  const [description, setDescription] = useState("");
  const [age, setAge] = useState("");
  const [title, setTitle] = useState("");
  const { id } = useParams();
  const navigate = useNavigate();

  const loadPet = async (petId) => {
    const response = await getPetById(petId);
    const pet = response.data;
    setName(pet.name);
    setDescription(pet.description);
    setAge(pet.age ?? "");
  };

  useEffect(() => {
    if (id) {
      setTitle("Update Pet");
      loadPet(id);
    } else {
      setTitle("Add Pet");
    }
  }, [id]);

  const saveOrUpdatePet = async (e) => {
    e.preventDefault();
    const pet = {
      name,
      description,
      age: age === "" ? null : Number(age),
    };

    if (name) {
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
      toast.error("Please fill in the pet name!");
    }
  };

  return {
    name,
    setName,
    description,
    setDescription,
    age,
    setAge,
    title,
    saveOrUpdatePet,
  };
};

export default usePetComponentHook;
