import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import { verifyPasswordForChange } from "../services/AuthService";

const LoginPasswordVerifyComponent = () => {
  const [username, setUsername] = useState("");
  const [currentPassword, setCurrentPassword] = useState("");
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      await verifyPasswordForChange(username, currentPassword);
      navigate("/login/change-password/set", {
        state: {
          verifiedStudent: true,
          username: username.trim(),
          currentPassword,
        },
      });
    } catch (err) {
      toast.error(err.response?.data?.message || "验证失败");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container mt-5" style={{ maxWidth: "420px" }}>
      <div className="card shadow-sm">
        <div className="card-body p-4">
          <h2 className="text-center mb-4">验证身份</h2>
          <p className="text-muted text-center mb-4">
            仅学生账号可修改密码，请先验证用户名与原密码
          </p>
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
              <label htmlFor="currentPassword" className="form-label">
                原密码
              </label>
              <input
                id="currentPassword"
                type="password"
                className="form-control"
                value={currentPassword}
                onChange={(e) => setCurrentPassword(e.target.value)}
                required
                autoComplete="current-password"
              />
            </div>
            <button
              type="submit"
              className="btn btn-primary w-100"
              disabled={loading}
            >
              {loading ? "验证中..." : "验证身份"}
            </button>
          </form>
          <div className="text-center mt-3">
            <Link to="/login">返回登录</Link>
          </div>
        </div>
      </div>
    </div>
  );
};

export default LoginPasswordVerifyComponent;
