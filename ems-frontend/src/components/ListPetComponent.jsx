import ButtonLink from "./ButtonLink";
import useListPetComponentHook from "../hooks/useListPetComponentHook";

const ListPetComponent = () => {
  const {
    pets,
    searchName,
    setSearchName,
    isSearching,
    adoptStudent,
    isAdoptMode,
    editPet,
    removePet,
    adoptPetForStudent,
    cancelAdopt,
    returnPet,
    searchPets,
    resetSearch,
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
      <form className="row g-2 align-items-end mb-3" onSubmit={searchPets}>
        <div className="col-md-6">
          <label htmlFor="petSearchName" className="form-label">
            按名称查询
          </label>
          <input
            id="petSearchName"
            type="text"
            className="form-control"
            placeholder="请输入宠物名称"
            value={searchName}
            onChange={(e) => setSearchName(e.target.value)}
          />
        </div>
        <div className="col-auto">
          <button type="submit" className="btn btn-outline-primary">
            查询
          </button>
        </div>
        {isSearching && (
          <div className="col-auto">
            <button
              type="button"
              className="btn btn-outline-secondary"
              onClick={resetSearch}
            >
              重置
            </button>
          </div>
        )}
      </form>
      <table className="table table-striped">
        <thead>
          <tr>
            <th scope="col">Name</th>
            <th scope="col">Description</th>
            <th scope="col">Age</th>
            <th scope="col">Category</th>
            <th scope="col">Adopted</th>
            <th scope="col">Adoption Count</th>
            <th scope="col">Return Count</th>
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
                <td>{item.adoptionCount ?? 0}</td>
                <td>{item.returnCount ?? 0}</td>
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
                      {item.adopted && (
                        <button
                          className="btn btn-outline-warning me-2"
                          onClick={() => returnPet(item)}
                        >
                          Return
                        </button>
                      )}
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
