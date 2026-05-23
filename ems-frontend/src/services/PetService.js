import axios from "axios";

const REST_API_URL = "http://localhost:8080/api/pets";

export const listPets = () => {
  return axios.get(REST_API_URL);
};

export const createPet = (pet) => {
  return axios.post(REST_API_URL, pet);
};

export const getPetById = (id) => {
  return axios.get(REST_API_URL + "/" + id);
};

export const updatePet = (id, pet) => {
  return axios.put(REST_API_URL + "/" + id, pet);
};

export const deletePet = (id) => {
  return axios.delete(REST_API_URL + "/" + id);
};
