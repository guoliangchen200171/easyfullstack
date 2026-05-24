import { useEffect, useState } from "react";
import {
  getMyDepartment,
  getMyDepartmentStudents,
} from "../services/DepartmentPortalService";

const DepartmentDashboardComponent = () => {
  const [department, setDepartment] = useState(null);
  const [students, setStudents] = useState([]);

  useEffect(() => {
    getMyDepartment().then((res) => setDepartment(res.data));
    getMyDepartmentStudents().then((res) => setStudents(res.data));
  }, []);

  if (!department) {
    return (
      <div className="container mt-5 text-center">
        <p>加载中...</p>
      </div>
    );
  }

  return (
    <div className="container mt-4">
      <h2 className="text-center mb-4">部门看板</h2>
      <div className="card mb-4">
        <div className="card-body">
          <h4>{department.departmentName}</h4>
          <p className="text-muted mb-0">{department.departmentDescription}</p>
        </div>
      </div>
      <h4 className="mb-3">本部门学生</h4>
      <table className="table table-striped">
        <thead>
          <tr>
            <th>First Name</th>
            <th>Last Name</th>
            <th>Email</th>
            <th>Pet</th>
            <th>Student Return Count</th>
            <th>Pet Adoption Count</th>
            <th>Pet Return Count</th>
          </tr>
        </thead>
        <tbody>
          {students.map((item) => (
            <tr key={item.id}>
              <td>{item.firstName}</td>
              <td>{item.lastName}</td>
              <td>{item.email}</td>
              <td>{item.petName || "No pet"}</td>
              <td>{item.returnCount ?? 0}</td>
              <td>{item.petAdoptionCount ?? 0}</td>
              <td>{item.petReturnCount ?? 0}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default DepartmentDashboardComponent;
