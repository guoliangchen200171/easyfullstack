import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import { registerDepartment } from "../services/AuthService";

const RegisterDepartmentComponent = () => {
  const [form, setForm] = useState({
    departmentName: "",
    departmentDescription: "",
    username: "",
    password: "",
  });
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      await registerDepartment(form);
      toast.success("部门注册成功，请登录");
      navigate("/login");
    } catch (err) {
      toast.error(err.response?.data?.message || "注册失败");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container mt-5" style={{ maxWidth: "520px" }}>
      <div className="card shadow-sm">
        <div className="card-body p-4">
          <h2 className="text-center mb-4">部门注册</h2>
          <form onSubmit={handleSubmit}>
            <div className="mb-3">
              <label className="form-label">部门名称</label>
              <input
                name="departmentName"
                className="form-control"
                value={form.departmentName}
                onChange={handleChange}
                required
              />
            </div>
            <div className="mb-3">
              <label className="form-label">部门描述</label>
              <input
                name="departmentDescription"
                className="form-control"
                value={form.departmentDescription}
                onChange={handleChange}
              />
            </div>
            <div className="mb-3">
              <label className="form-label">登录用户名</label>
              <input
                name="username"
                className="form-control"
                value={form.username}
                onChange={handleChange}
                required
              />
            </div>
            <div className="mb-4">
              <label className="form-label">登录密码</label>
              <input
                name="password"
                type="password"
                className="form-control"
                value={form.password}
                onChange={handleChange}
                required
              />
            </div>
            <button
              type="submit"
              className="btn btn-success w-100"
              disabled={loading}
            >
              {loading ? "注册中..." : "注册"}
            </button>
          </form>
          <p className="text-center mt-3 mb-0">
            <Link to="/login">返回登录</Link>
          </p>
        </div>
      </div>
    </div>
  );
};

export default RegisterDepartmentComponent;
