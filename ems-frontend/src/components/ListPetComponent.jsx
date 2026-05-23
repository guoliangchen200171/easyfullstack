import ButtonLink from "./ButtonLink";
import useListPetComponentHook from "../hooks/useListPetComponentHook";

const ListPetComponent = () => {
  const {
    pets,
    adoptStudent,
    isAdoptMode,
    editPet,
    removePet,
    adoptPetForStudent,
    cancelAdopt,
  } = useListPetComponentHook();

  return (
    <div className="container">
      <h2 className="text-center py-3">List of Pets</h2>
      {isAdoptMode && adoptStudent && (
        <div className="alert alert-info">
          正在为 {adoptStudent.firstName} {adoptStudent.lastName} 领养宠物，请选择一只未领养的宠物。
          <button
            className="btn btn-sm btn-outline-secondary ms-3"
            onClick={cancelAdopt}
          >
            取消
          </button>
        </div>
      )}
      {!isAdoptMode && <ButtonLink text="Add Pet" toAction="/add-pet" />}
      <table className="table table-striped">
        <thead>
          <tr>
            <th scope="col">Name</th>
            <th scope="col">Description</th>
            <th scope="col">Age</th>
            <th scope="col">Category</th>
            <th scope="col">Adopted</th>
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
                <td>{item.category}</td>
                <td>{item.adopted ? "Yes" : "No"}</td>
                <td>
                  {isAdoptMode ? (
                    <button
                      className="btn btn-outline-success"
                      onClick={() => adoptPetForStudent(item)}
                      disabled={item.adopted}
                    >
                      Adopt
                    </button>
                  ) : (
                    <>
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
                    </>
                  )}
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
