import { useState } from "react";
import { toast } from "react-toastify";
import { changePassword } from "../services/AuthService";

const ChangePasswordComponent = () => {
  const [currentPassword, setCurrentPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [loading, setLoading] = useState(false);

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
      await changePassword(currentPassword, newPassword);
      toast.success("密码修改成功");
      setCurrentPassword("");
      setNewPassword("");
      setConfirmPassword("");
    } catch (err) {
      toast.error(err.response?.data?.message || "密码修改失败");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container mt-4">
      <div className="card col-md-6 offset-md-3">
        <div className="card-body">
          <h2 className="text-center mb-4">修改密码</h2>
          <form onSubmit={handleSubmit}>
            <div className="mb-3">
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
        </div>
      </div>
    </div>
  );
};

export default ChangePasswordComponent;
