import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { listPets, deletePet } from "../services/PetService";
import { toast } from "react-toastify";

const useListPetComponentHook = () => {
  const [pets, setPets] = useState([]);
  const navigate = useNavigate();

  const getPets = async () => {
    try {
      const response = await listPets();
      setPets(response.data);
    } catch (err) {
      console.log(err);
    }
  };

  const editPet = (id) => {
    navigate(`/edit-pet/${id}`);
  };

  const removePet = async (id) => {
    await deletePet(id);
    toast.error("Pet deleted successfully!");
    getPets();
  };

  useEffect(() => {
    getPets();
  }, []);

  return {
    pets,
    getPets,
    editPet,
    removePet,
  };
};

export default useListPetComponentHook;
