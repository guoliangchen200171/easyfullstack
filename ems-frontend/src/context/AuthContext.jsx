import { createContext, useContext, useState, useCallback, useEffect } from "react";
import { checkAuth as checkAuthApi } from "../services/AuthService";

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [auth, setAuth] = useState(null);
  const [loading, setLoading] = useState(true);

  const loadAuth = useCallback(async () => {
    try {
      const response = await checkAuthApi();
      setAuth(response.data);
      return response.data;
    } catch {
      setAuth(null);
      return null;
    } finally {
      setLoading(false);
    }
  }, []);

  const setAuthUser = (user) => {
    setAuth(user);
    setLoading(false);
  };

  const clearAuth = () => {
    setAuth(null);
  };

  const getHomePath = (role) => {
    switch (role) {
      case "STUDENT":
        return "/student/profile";
      case "DEPARTMENT":
        return "/department/dashboard";
      default:
        return "/products";
    }
  };

  useEffect(() => {
    loadAuth();
  }, [loadAuth]);

  return (
    <AuthContext.Provider
      value={{ auth, loading, loadAuth, setAuthUser, clearAuth, getHomePath }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
