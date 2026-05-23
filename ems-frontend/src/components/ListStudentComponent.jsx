import ButtonLink from "./ButtonLink";
import useListStudentComponentHook from "../hooks/useListStudentComponentHook";

const ListStudentComponent = () => {
  const { students, getDepartmentName, updateStudent, adoptPetForStudent, returnPetForStudent, deleteStudentById } =
    useListStudentComponentHook();

  return (
    <div className="container">
      <h2 className="text-center my-3">List of Students</h2>
      <ButtonLink text="Add Student" toAction="/add-student" />
      <table className="table table-striped">
        <thead>
          <tr>
            <th scope="col">First Name</th>
            <th scope="col">Last Name</th>
            <th scope="col">Email</th>
            <th scope="col">Department</th>
            <th scope="col">Pet</th>
            {students.length > 0 && <th scope="col">Action</th>}
          </tr>
        </thead>
        <tbody>
          {students.map((item) => {
            return (
              <tr key={item.id}>
                <td>{item.firstName}</td>
                <td>{item.lastName}</td>
                <td>{item.email}</td>
                <td>{getDepartmentName(item.departmentId)}</td>
                <td>{item.petName || "No pet"}</td>
                {students.length > 0 && (
                  <td>
                    {item.petId ? (
                      <button
                        className="btn btn-outline-warning me-2"
                        onClick={() => returnPetForStudent(item)}
                      >
                        Return Pet
                      </button>
                    ) : (
                      <button
                        className="btn btn-outline-success me-2"
                        onClick={() => adoptPetForStudent(item)}
                      >
                        Adopt
                      </button>
                    )}
                    <button
                      className="btn btn-outline-info me-2"
                      onClick={() => updateStudent(item.id)}
                    >
                      Update
                    </button>
                    <button
                      className="btn btn-outline-danger"
                      onClick={() => deleteStudentById(item.id)}
                    >
                      Delete
                    </button>
                  </td>
                )}
              </tr>
            );
          })}
        </tbody>
      </table>
    </div>
  );
};

export default ListStudentComponent;
