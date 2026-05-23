import ButtonLink from "./ButtonLink";
import usePetComponentHook from "../hooks/usePetComponentHook";

const PetComponent = () => {
  const {
    name,
    setName,
    description,
    setDescription,
    age,
    setAge,
    title,
    saveOrUpdatePet,
  } = usePetComponentHook();

  return (
    <div className="container mt-5">
      <ButtonLink text="Go Back" toAction="/pets" />
      <div className="row">
        <div className="card col-md-6 offset-md-3 offset-md-3">
          <h2 className="text-center">{title}</h2>
          <div className="card-body">
            <form>
              <div className="form-group mb-2">
                <label className="form-label">Name: </label>
                <input
                  type="text"
                  name="name"
                  placeholder="Enter Pet Name"
                  className="form-control"
                  value={name}
                  onChange={(e) => setName(e.target.value)}
                />
              </div>
              <div className="form-group mb-2">
                <label className="form-label">Description: </label>
                <input
                  type="text"
                  name="description"
                  placeholder="Enter Description"
                  className="form-control"
                  value={description}
                  onChange={(e) => setDescription(e.target.value)}
                />
              </div>
              <div className="form-group mb-2">
                <label className="form-label">Age: </label>
                <input
                  type="number"
                  name="age"
                  placeholder="Enter Age"
                  className="form-control"
                  value={age}
                  onChange={(e) => setAge(e.target.value)}
                />
              </div>
              <button
                className="btn btn-outline-success"
                onClick={saveOrUpdatePet}
              >
                Submit
              </button>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
};

export default PetComponent;
