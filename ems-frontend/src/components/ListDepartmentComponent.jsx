import ButtonLink from "./ButtonLink";
import PaginationBar from "./PaginationBar";
import useListDepartmentComponentHook from "../hooks/useListDepartmentComponentHook";

const ListDepartmentComponent = () => {
  const {
    departments,
    page,
    totalPages,
    totalElements,
    updateDepartment,
    removeDepartment,
    handlePageChange,
  } = useListDepartmentComponentHook();

  return (
    <div className="container">
      <h2 className="text-center py-3">List of Departments</h2>
      <ButtonLink text="Add Department" toAction="/add-department" />
      <ButtonLink text="按 ID 查询" toAction="/view-department" />
      <table className="table table-striped">
        <thead>
          <tr>
            <th scope="col">Department Name</th>
            <th scope="col">Department Description</th>
            <th scope="col">Action</th>
          </tr>
        </thead>
        <tbody>
          {departments.map((item) => {
            return (
              <tr key={item.id}>
                <td>{item.departmentName}</td>
                <td>{item.departmentDescription}</td>
                <td>
                  <button
                    className="btn btn-outline-info me-2"
                    onClick={() => updateDepartment(item.id)}
                  >
                    Update
                  </button>
                  <button
                    className="btn btn-outline-danger"
                    onClick={() => removeDepartment(item.id)}
                  >
                    Delete
                  </button>
                </td>
              </tr>
            );
          })}
        </tbody>
      </table>
      <PaginationBar
        page={page}
        totalPages={totalPages}
        totalElements={totalElements}
        onPageChange={handlePageChange}
      />
    </div>
  );
};

export default ListDepartmentComponent;
