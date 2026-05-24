import { Navigate, Outlet } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

const RoleRoute = ({ role }) => {
  const { auth, loading, getHomePath } = useAuth();

  if (loading) {
    return (
      <div className="container mt-5 text-center">
        <p>加载中...</p>
      </div>
    );
  }

  if (!auth?.authenticated) {
    return <Navigate to="/login" replace />;
  }

  if (auth.role !== role) {
    return <Navigate to={getHomePath(auth.role)} replace />;
  }

  return <Outlet />;
};

export default RoleRoute;
