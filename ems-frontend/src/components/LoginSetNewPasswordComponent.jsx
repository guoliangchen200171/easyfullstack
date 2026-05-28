import { useState } from "react";
import { Link, Navigate, useLocation, useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import { studentChangePasswordPublic } from "../services/AuthService";

const LoginSetNewPasswordComponent = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { verifiedStudent, username, currentPassword } = location.state || {};

  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [loading, setLoading] = useState(false);

  if (verifiedStudent !== true || !username || !currentPassword) {
    return <Navigate to="/login/change-password" replace />;
  }

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (newPassword.length < 6) {
      toast.error("新密码长度不能少于 6 位");
      return;
    }
    if (newPassword !== confirmPassword) {
      toast.error("两次输入的新密码不一致");
      return;
    }
    if (currentPassword === newPassword) {
      toast.error("新密码不能与原密码相同");
      return;
    }

    setLoading(true);
    try {
      await studentChangePasswordPublic(username, currentPassword, newPassword);
      toast.success("密码修改成功，请使用新密码登录");
      navigate("/login", { replace: true });
    } catch (err) {
      toast.error(err.response?.data?.message || "密码修改失败");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container mt-5" style={{ maxWidth: "420px" }}>
      <div className="card shadow-sm">
        <div className="card-body p-4">
          <h2 className="text-center mb-4">设置新密码</h2>
          <p className="text-muted text-center mb-4">账号：{username}</p>
          <form onSubmit={handleSubmit}>
            <div className="mb-3">
              <label htmlFor="newPassword" className="form-label">
                新密码
              </label>
              <input
                id="newPassword"
                type="password"
                className="form-control"
                value={newPassword}
                onChange={(e) => setNewPassword(e.target.value)}
                required
                minLength={6}
                autoComplete="new-password"
              />
            </div>
            <div className="mb-4">
              <label htmlFor="confirmPassword" className="form-label">
                确认新密码
              </label>
              <input
                id="confirmPassword"
                type="password"
                className="form-control"
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)}
                required
                minLength={6}
                autoComplete="new-password"
              />
            </div>
            <button
              type="submit"
              className="btn btn-primary w-100"
              disabled={loading}
            >
              {loading ? "提交中..." : "确认修改"}
            </button>
          </form>
          <div className="text-center mt-3">
            <Link to="/login/change-password">返回上一步</Link>
            <span className="mx-2">|</span>
            <Link to="/login">返回登录</Link>
          </div>
        </div>
      </div>
    </div>
  );
};

export default LoginSetNewPasswordComponent;
