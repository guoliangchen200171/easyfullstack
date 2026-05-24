import { NavLink, useNavigate } from "react-router-dom";
import { logout } from "../services/AuthService";
import { useAuth } from "../context/AuthContext";
import { toast } from "react-toastify";

const HeaderComponent = () => {
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
    <div>
      <header>
        <nav className="navbar navbar-expand-lg navbar-dark bg-dark">
          <a className="navbar-brand" href="#">
            宠物领养系统
          </a>
          <div className="collapse navbar-collapse" id="navbarNav">
            <ul className="navbar-nav me-auto">
              <li className="nav-item">
                <NavLink className="nav-link" to="/students">
                  Students
                </NavLink>
              </li>
              <li className="nav-item">
                <NavLink className="nav-link" to="/departments">
                  Departments
                </NavLink>
              </li>
              <li className="nav-item">
                <NavLink className="nav-link" to="/pets">
                  Pets
                </NavLink>
              </li>
              <li className="nav-item">
                <NavLink className="nav-link" to="/adoption-history">
                  Adoption History
                </NavLink>
              </li>
            </ul>
            <button
              className="btn btn-outline-light btn-sm"
              onClick={handleLogout}
            >
              Logout
            </button>
          </div>
        </nav>
      </header>
    </div>
  );
};

export default HeaderComponent;
