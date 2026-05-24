import { useEffect, useState } from "react";
import { getMyProfile } from "../services/StudentPortalService";

const StudentProfileComponent = () => {
  const [profile, setProfile] = useState(null);

  useEffect(() => {
    getMyProfile().then((res) => setProfile(res.data));
  }, []);

  if (!profile) {
    return (
      <div className="container mt-5 text-center">
        <p>加载中...</p>
      </div>
    );
  }

  return (
    <div className="container mt-4">
      <h2 className="text-center mb-4">我的信息</h2>
      <div className="card col-md-8 offset-md-2">
        <div className="card-body">
          <table className="table table-bordered mb-0">
            <tbody>
              <tr>
                <th scope="row">First Name</th>
                <td>{profile.firstName}</td>
              </tr>
              <tr>
                <th scope="row">Last Name</th>
                <td>{profile.lastName}</td>
              </tr>
              <tr>
                <th scope="row">Email</th>
                <td>{profile.email}</td>
              </tr>
              <tr>
                <th scope="row">Department</th>
                <td>{profile.departmentName || "Unknown"}</td>
              </tr>
              <tr>
                <th scope="row">Pet</th>
                <td>{profile.petName || "No pet"}</td>
              </tr>
              <tr>
                <th scope="row">Return Count</th>
                <td>{profile.returnCount ?? 0}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};

export default StudentProfileComponent;
