import api from "./api";

const REST_API_URL = "/api/pets";

export const listPets = () => {
  return api.get(REST_API_URL);
};

export const searchPetsByName = (name) => {
  return api.get(`${REST_API_URL}/search`, { params: { name } });
};

export const createPet = (pet) => {
  return api.post(REST_API_URL, pet);
};

export const getPetById = (id) => {
  return api.get(REST_API_URL + "/" + id);
};

export const updatePet = (id, pet) => {
  return api.put(REST_API_URL + "/" + id, pet);
};

export const deletePet = (id) => {
  return api.delete(REST_API_URL + "/" + id);
};

export const adoptPet = (petId, studentId) => {
  return api.put(REST_API_URL + "/" + petId + "/adopt/" + studentId);
};

export const returnPetById = (petId) => {
  return api.put(REST_API_URL + "/" + petId + "/return");
};
