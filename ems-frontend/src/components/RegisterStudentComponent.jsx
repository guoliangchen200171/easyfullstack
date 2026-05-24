import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import { registerStudent } from "../services/AuthService";
import { listDepartments } from "../services/DepartmentService";

const RegisterStudentComponent = () => {
  const [form, setForm] = useState({
    firstName: "",
    lastName: "",
    email: "",
    departmentId: "",
  });
  const [departments, setDepartments] = useState([]);
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    listDepartments()
      .then((res) => setDepartments(res.data))
      .catch(() => toast.error("无法加载部门列表"));
  }, []);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      await registerStudent({
        ...form,
        departmentId: Number(form.departmentId),
      });
      toast.success("学生注册成功，默认密码为 123");
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
          <h2 className="text-center mb-4">学生注册</h2>
          <p className="text-muted text-center mb-4">注册后默认密码为 123</p>
          <form onSubmit={handleSubmit}>
            <div className="mb-3">
              <label className="form-label">First Name</label>
              <input
                name="firstName"
                className="form-control"
                value={form.firstName}
                onChange={handleChange}
                required
              />
            </div>
            <div className="mb-3">
              <label className="form-label">Last Name</label>
              <input
                name="lastName"
                className="form-control"
                value={form.lastName}
                onChange={handleChange}
                required
              />
            </div>
            <div className="mb-3">
              <label className="form-label">Email（登录用户名）</label>
              <input
                name="email"
                type="email"
                className="form-control"
                value={form.email}
                onChange={handleChange}
                required
              />
            </div>
            <div className="mb-4">
              <label className="form-label">Department</label>
              <select
                name="departmentId"
                className="form-select"
                value={form.departmentId}
                onChange={handleChange}
                required
              >
                <option value="">请选择部门</option>
                {departments.map((dept) => (
                  <option key={dept.id} value={dept.id}>
                    {dept.departmentName}
                  </option>
                ))}
              </select>
            </div>
            <button
              type="submit"
              className="btn btn-primary w-100"
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

export default RegisterStudentComponent;
