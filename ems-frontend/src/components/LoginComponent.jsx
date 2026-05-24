import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import { login } from "../services/AuthService";
import { useAuth } from "../context/AuthContext";

const LoginComponent = () => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();
  const { setAuthUser, getHomePath } = useAuth();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      const response = await login(username, password);
      setAuthUser(response.data);
      toast.success("登录成功");
      navigate(getHomePath(response.data.role));
    } catch (err) {
      const message =
        err.response?.data?.message ||
        (err.response
          ? "账号或密码错误"
          : "无法连接服务器，请确认后端已启动");
      toast.error(message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container mt-5" style={{ maxWidth: "420px" }}>
      <div className="card shadow-sm">
        <div className="card-body p-4">
          <h2 className="text-center mb-4">登录</h2>
          <p className="text-muted text-center mb-4">宠物领养系统</p>
          <form onSubmit={handleSubmit}>
            <div className="mb-3">
              <label htmlFor="username" className="form-label">
                用户名
              </label>
              <input
                id="username"
                type="text"
                className="form-control"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                required
                autoComplete="username"
              />
            </div>
            <div className="mb-4">
              <label htmlFor="password" className="form-label">
                密码
              </label>
              <input
                id="password"
                type="password"
                className="form-control"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
                autoComplete="current-password"
              />
            </div>
            <button
              type="submit"
              className="btn btn-primary w-100"
              disabled={loading}
            >
              {loading ? "登录中..." : "登录"}
            </button>
          </form>
          <div className="text-center mt-3">
            <Link to="/register/student" className="me-3">
              学生注册
            </Link>
            <Link to="/register/department">部门注册</Link>
          </div>
        </div>
      </div>
    </div>
  );
};

export default LoginComponent;
