import "./App.css";
import HeaderComponent from "./components/HeaderComponent";
import ListStudentComponent from "./components/ListStudentComponent";
import { BrowserRouter, Routes, Route, Navigate, Outlet } from "react-router-dom";
import StudentComponent from "./components/StudentComponent";
import ListDepartmentComponent from "./components/ListDepartmentComponent";
import DepartmentComponent from "./components/DepartmentComponent";
import ViewDepartmentComponent from "./components/ViewDepartmentComponent";
import ListPetComponent from "./components/ListPetComponent";
import PetComponent from "./components/PetComponent";
import ListAdoptionHistoryComponent from "./components/ListAdoptionHistoryComponent";
import LoginComponent from "./components/LoginComponent";
import ProtectedRoute from "./components/ProtectedRoute";

const AppLayout = () => (
  <>
    <HeaderComponent />
    <Outlet />
  </>
);

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<LoginComponent />} />
        <Route element={<ProtectedRoute />}>
          <Route element={<AppLayout />}>
            <Route path="/" element={<Navigate to="/students" replace />} />
            <Route path="/students" element={<ListStudentComponent />} />
            <Route path="/add-student" element={<StudentComponent />} />
            <Route path="/edit-student/:id" element={<StudentComponent />} />
            <Route path="/departments" element={<ListDepartmentComponent />} />
            <Route path="/add-department" element={<DepartmentComponent />} />
            <Route path="/edit-department/:id" element={<DepartmentComponent />} />
            <Route path="/view-department" element={<ViewDepartmentComponent />} />
            <Route path="/pets" element={<ListPetComponent />} />
            <Route path="/add-pet" element={<PetComponent />} />
            <Route path="/edit-pet/:id" element={<PetComponent />} />
            <Route path="/adoption-history" element={<ListAdoptionHistoryComponent />} />
          </Route>
        </Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;
