import { NavLink, useNavigate } from "react-router-dom";
import { logout } from "../services/AuthService";
import { useAuth } from "../context/AuthContext";
import { toast } from "react-toastify";

const DepartmentHeader = () => {
  const navigate = useNavigate();
  const { clearAuth } = useAuth();

  const handleLogout = async () => {
    try {
      await logout();
      toast.success("已退出登录");
    } catch {
      toast.info("已退出登录");
    } finally {
      clearAuth();
      navigate("/login");
    }
  };

  return (
    <header>
      <nav className="navbar navbar-expand-lg navbar-dark bg-success">
        <span className="navbar-brand ms-3">部门门户</span>
        <div className="collapse navbar-collapse">
          <ul className="navbar-nav me-auto">
            <li className="nav-item">
              <NavLink className="nav-link" to="/department/dashboard">
                部门看板
              </NavLink>
            </li>
            <li className="nav-item">
              <NavLink className="nav-link" to="/department/change-password">
                修改密码
              </NavLink>
            </li>
          </ul>
          <button
            className="btn btn-outline-light btn-sm me-3"
            onClick={handleLogout}
          >
            Logout
          </button>
        </div>
      </nav>
    </header>
  );
};

export default DepartmentHeader;
