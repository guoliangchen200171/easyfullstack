import ButtonLink from "./ButtonLink";
import PaginationBar from "./PaginationBar";
import useListStudentComponentHook from "../hooks/useListStudentComponentHook";

const ListStudentComponent = () => {
  const {
    students,
    page,
    totalPages,
    totalElements,
    getDepartmentName,
    updateStudent,
    adoptPetForStudent,
    returnPetForStudent,
    deleteStudentById,
    resetReturnCountForStudent,
    handleAddDeposit,
    handlePageChange,
    depositEmail,
    setDepositEmail,
    depositAmount,
    setDepositAmount,
  } = useListStudentComponentHook();

  const formatDeposit = (deposit) => {
    const value = Number(deposit ?? 0);
    return Number.isNaN(value) ? "0.00" : value.toFixed(2);
  };

  return (
    <div className="container">
      <h2 className="text-center my-3">List of Students</h2>
      <ButtonLink text="Add Student" toAction="/add-student" />

      <div className="card mb-4">
        <div className="card-body">
          <h5 className="card-title">存款操作</h5>
          <div className="row g-3 align-items-end">
            <div className="col-md-3">
              <label htmlFor="depositEmail" className="form-label">
                邮箱
              </label>
              <input
                id="depositEmail"
                type="email"
                className="form-control"
                placeholder="请输入学生邮箱"
                value={depositEmail}
                onChange={(e) => setDepositEmail(e.target.value)}
              />
            </div>
            <div className="col-md-3">
              <label htmlFor="depositAmount" className="form-label">
                金额（元）
              </label>
              <input
                id="depositAmount"
                type="number"
                min="0.01"
                step="0.01"
                className="form-control"
                placeholder="请输入金额"
                value={depositAmount}
                onChange={(e) => setDepositAmount(e.target.value)}
              />
            </div>
            <div className="col-auto">
              <button
                type="button"
                className="btn btn-outline-success"
                onClick={handleAddDeposit}
              >
                加存款
              </button>
            </div>
          </div>
        </div>
      </div>

      <table className="table table-striped">
        <thead>
          <tr>
            <th scope="col">First Name</th>
            <th scope="col">Last Name</th>
            <th scope="col">Email</th>
            <th scope="col">Department</th>
            <th scope="col">Pet</th>
            <th scope="col">Return Count</th>
            <th scope="col">存款</th>
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
                <td>{item.returnCount ?? 0}</td>
                <td>{formatDeposit(item.deposit)}</td>
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
                      className="btn btn-outline-secondary me-2"
                      onClick={() => resetReturnCountForStudent(item)}
                    >
                      Reset Return Count
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
      <PaginationBar
        page={page}
        totalPages={totalPages}
        totalElements={totalElements}
        onPageChange={handlePageChange}
      />
    </div>
  );
};

export default ListStudentComponent;
