import "./App.css";
import HeaderComponent from "./components/HeaderComponent";
import ListStudentComponent from "./components/ListStudentComponent";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import StudentComponent from "./components/StudentComponent";
import ListDepartmentComponent from "./components/ListDepartmentComponent";
import DepartmentComponent from "./components/DepartmentComponent";
import ViewDepartmentComponent from "./components/ViewDepartmentComponent";
import ListPetComponent from "./components/ListPetComponent";
import PetComponent from "./components/PetComponent";

function App() {
  return (
    <>
      <BrowserRouter>
        <HeaderComponent />
        <Routes>
          <Route path="/" element={<ListStudentComponent />} />
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
        </Routes>
      </BrowserRouter>
    </>
  );
}

export default App;
