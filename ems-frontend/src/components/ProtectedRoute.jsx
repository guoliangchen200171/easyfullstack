import { useEffect, useState } from "react";
import { Navigate, Outlet } from "react-router-dom";
import { checkAuth } from "../services/AuthService";

const ProtectedRoute = () => {
  const [authState, setAuthState] = useState("loading");

  useEffect(() => {
    const verifyAuth = async () => {
      try {
        await checkAuth();
        setAuthState("authenticated");
      } catch {
        setAuthState("unauthenticated");
      }
    };

    verifyAuth();
  }, []);

  if (authState === "loading") {
    return (
      <div className="container mt-5 text-center">
        <p>加载中...</p>
      </div>
    );
  }

  if (authState === "unauthenticated") {
    return <Navigate to="/login" replace />;
  }

  return <Outlet />;
};

export default ProtectedRoute;
