import { useEffect, useState } from "react";
import { getMyProfile } from "../services/StudentPortalService";
import { formatMembershipLevel } from "../utils/formatMembershipLevel";

const StudentProfileComponent = () => {
  const [profile, setProfile] = useState(null);
  const [loadError, setLoadError] = useState(null);

  useEffect(() => {
    setLoadError(null);
    getMyProfile()
      .then((res) => setProfile(res.data))
      .catch(() => setLoadError("会员信息加载失败，请确认后端已启动并重新登录"));
  }, []);

  if (loadError) {
    return (
      <div className="container mt-5">
        <div className="alert alert-danger text-center">{loadError}</div>
      </div>
    );
  }

  if (!profile) {
    return (
      <div className="container mt-5 text-center">
        <p>加载中...</p>
      </div>
    );
  }

  const levelDisplay = formatMembershipLevel(
    profile.membershipLevelName,
    profile.membershipLevel
  );

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
              <tr>
                <th scope="row">存款</th>
                <td>{Number(profile.deposit ?? 0).toFixed(2)} 元</td>
              </tr>
              <tr>
                <th scope="row">会员积分</th>
                <td>{profile.membershipPoints ?? 0}</td>
              </tr>
              <tr>
                <th scope="row">会员等级</th>
                <td>{levelDisplay}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};

export default StudentProfileComponent;
