import ButtonLink from "./ButtonLink";
import useViewDepartmentComponentHook from "../hooks/useViewDepartmentComponentHook";

const ViewDepartmentComponent = () => {
  const {
    departmentIdInput,
    setDepartmentIdInput,
    department,
    students,
    loading,
    searchDepartment,
  } = useViewDepartmentComponentHook();

  return (
    <div className="container mt-5">
      <ButtonLink text="返回部门列表" toAction="/departments" />
      <div className="row">
        <div className="card col-md-8 offset-md-2">
          <h2 className="text-center py-3">按 ID 查询部门</h2>
          <div className="card-body">
            <form onSubmit={searchDepartment}>
              <div className="form-group mb-3">
                <label className="form-label">部门 ID：</label>
                <input
                  type="number"
                  name="departmentId"
                  placeholder="请输入部门 ID"
                  className="form-control"
                  value={departmentIdInput}
                  onChange={(e) => setDepartmentIdInput(e.target.value)}
                  min="1"
                />
              </div>
              <button
                type="submit"
                className="btn btn-outline-primary"
                disabled={loading}
              >
                {loading ? "查询中..." : "查询"}
              </button>
            </form>

            {department && (
              <div className="mt-4">
                <h4>部门信息</h4>
                <table className="table table-bordered">
                  <tbody>
                    <tr>
                      <th scope="row">ID</th>
                      <td>{department.id}</td>
                    </tr>
                    <tr>
                      <th scope="row">部门名称</th>
                      <td>{department.departmentName}</td>
                    </tr>
                    <tr>
                      <th scope="row">部门描述</th>
                      <td>{department.departmentDescription}</td>
                    </tr>
                  </tbody>
                </table>

                <h4 className="mt-3">部门学生</h4>
                {students.length > 0 ? (
                  <table className="table table-striped">
                    <thead>
                      <tr>
                        <th scope="col">ID</th>
                        <th scope="col">姓</th>
                        <th scope="col">名</th>
                        <th scope="col">邮箱</th>
                      </tr>
                    </thead>
                    <tbody>
                      {students.map((student) => (
                        <tr key={student.id}>
                          <td>{student.id}</td>
                          <td>{student.firstName}</td>
                          <td>{student.lastName}</td>
                          <td>{student.email}</td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                ) : (
                  <p className="text-muted">该部门暂无学生</p>
                )}
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default ViewDepartmentComponent;
