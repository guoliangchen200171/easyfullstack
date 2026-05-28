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
        <nav className="navbar navbar-dark bg-dark px-3 py-2">
          <span className="navbar-brand mb-0 me-3">学生宠物系统</span>
          <div className="d-flex flex-wrap align-items-center flex-grow-1 ms-3 gap-3">
            <ul className="navbar-nav flex-row flex-wrap me-auto gap-2 gap-md-3">
              <li className="nav-item">
                <NavLink className="nav-link px-2 px-md-3" to="/products">
                  商品管理
                </NavLink>
              </li>
              <li className="nav-item">
                <NavLink className="nav-link px-2 px-md-3" to="/membership-levels">
                  会员等级
                </NavLink>
              </li>
              <li className="nav-item">
                <NavLink className="nav-link px-2 px-md-3" to="/students">
                  Students
                </NavLink>
              </li>
              <li className="nav-item">
                <NavLink className="nav-link px-2 px-md-3" to="/departments">
                  Departments
                </NavLink>
              </li>
              <li className="nav-item">
                <NavLink className="nav-link px-2 px-md-3" to="/pets">
                  Pets
                </NavLink>
              </li>
              <li className="nav-item">
                <NavLink className="nav-link px-2 px-md-3" to="/adoption-requests">
                  领养审批
                </NavLink>
              </li>
              <li className="nav-item">
                <NavLink className="nav-link px-2 px-md-3" to="/adoption-history">
                  Adoption History
                </NavLink>
              </li>
              <li className="nav-item">
                <NavLink className="nav-link px-2 px-md-3" to="/product-orders">
                  下单记录
                </NavLink>
              </li>
            </ul>
            <button
              type="button"
              className="btn btn-outline-light btn-sm ms-2 ms-md-3"
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
