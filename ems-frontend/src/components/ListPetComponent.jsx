import ButtonLink from "./ButtonLink";
import useListPetComponentHook from "../hooks/useListPetComponentHook";

const ListPetComponent = () => {
  const { pets, editPet, removePet } = useListPetComponentHook();

  return (
    <div className="container">
      <h2 className="text-center py-3">List of Pets</h2>
      <ButtonLink text="Add Pet" toAction="/add-pet" />
      <table className="table table-striped">
        <thead>
          <tr>
            <th scope="col">Name</th>
            <th scope="col">Description</th>
            <th scope="col">Age</th>
            <th scope="col">Action</th>
          </tr>
        </thead>
        <tbody>
          {pets.map((item) => {
            return (
              <tr key={item.id}>
                <td>{item.name}</td>
                <td>{item.description}</td>
                <td>{item.age}</td>
                <td>
                  <button
                    className="btn btn-outline-info me-2"
                    onClick={() => editPet(item.id)}
                  >
                    Update
                  </button>
                  <button
                    className="btn btn-outline-danger"
                    onClick={() => removePet(item.id)}
                  >
                    Delete
                  </button>
                </td>
              </tr>
            );
          })}
        </tbody>
      </table>
    </div>
  );
};

export default ListPetComponent;
