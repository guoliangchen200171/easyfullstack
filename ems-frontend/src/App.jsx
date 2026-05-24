import "./App.css";
import HeaderComponent from "./components/HeaderComponent";
import StudentHeader from "./components/StudentHeader";
import DepartmentHeader from "./components/DepartmentHeader";
import ListStudentComponent from "./components/ListStudentComponent";
import {
  BrowserRouter,
  Routes,
  Route,
  Navigate,
  Outlet,
} from "react-router-dom";
import StudentComponent from "./components/StudentComponent";
import ListDepartmentComponent from "./components/ListDepartmentComponent";
import DepartmentComponent from "./components/DepartmentComponent";
import ViewDepartmentComponent from "./components/ViewDepartmentComponent";
import ListPetComponent from "./components/ListPetComponent";
import PetComponent from "./components/PetComponent";
import ListAdoptionHistoryComponent from "./components/ListAdoptionHistoryComponent";
import LoginComponent from "./components/LoginComponent";
import RoleRoute from "./components/RoleRoute";
import StudentProfileComponent from "./components/StudentProfileComponent";
import StudentPetListComponent from "./components/StudentPetListComponent";
import DepartmentDashboardComponent from "./components/DepartmentDashboardComponent";
import RegisterDepartmentComponent from "./components/RegisterDepartmentComponent";
import RegisterStudentComponent from "./components/RegisterStudentComponent";
import AdoptionApprovalComponent from "./components/AdoptionApprovalComponent";

const AdminLayout = () => (
  <>
    <HeaderComponent />
    <Outlet />
  </>
);

const StudentLayout = () => (
  <>
    <StudentHeader />
    <Outlet />
  </>
);

const DepartmentLayout = () => (
  <>
    <DepartmentHeader />
    <Outlet />
  </>
);

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<LoginComponent />} />
        <Route
          path="/register/department"
          element={<RegisterDepartmentComponent />}
        />
        <Route path="/register/student" element={<RegisterStudentComponent />} />

        <Route element={<RoleRoute role="ADMIN" />}>
          <Route element={<AdminLayout />}>
            <Route path="/" element={<Navigate to="/students" replace />} />
            <Route path="/students" element={<ListStudentComponent />} />
            <Route path="/add-student" element={<StudentComponent />} />
            <Route path="/edit-student/:id" element={<StudentComponent />} />
            <Route path="/departments" element={<ListDepartmentComponent />} />
            <Route path="/add-department" element={<DepartmentComponent />} />
            <Route
              path="/edit-department/:id"
              element={<DepartmentComponent />}
            />
            <Route
              path="/view-department"
              element={<ViewDepartmentComponent />}
            />
            <Route path="/pets" element={<ListPetComponent />} />
            <Route path="/add-pet" element={<PetComponent />} />
            <Route path="/edit-pet/:id" element={<PetComponent />} />
            <Route
              path="/adoption-history"
              element={<ListAdoptionHistoryComponent />}
            />
            <Route
              path="/adoption-requests"
              element={<AdoptionApprovalComponent />}
            />
          </Route>
        </Route>

        <Route element={<RoleRoute role="STUDENT" />}>
          <Route element={<StudentLayout />}>
            <Route
              path="/student"
              element={<Navigate to="/student/profile" replace />}
            />
            <Route
              path="/student/profile"
              element={<StudentProfileComponent />}
            />
            <Route path="/student/pets" element={<StudentPetListComponent />} />
          </Route>
        </Route>

        <Route element={<RoleRoute role="DEPARTMENT" />}>
          <Route element={<DepartmentLayout />}>
            <Route
              path="/department/dashboard"
              element={<DepartmentDashboardComponent />}
            />
          </Route>
        </Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;
