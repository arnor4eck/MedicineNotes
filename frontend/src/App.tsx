import './App.css'
import AuthPage from "./pages/auth/AuthPage.jsx"
import Logout from "./pages/auth/Logout.jsx"
import TemplatesPage from "./pages/templates/TemplatesPage.jsx"
import TemplateDetail from "./pages/templates/TemplateDetail.jsx"
import CreateTemplate from "./pages/templates/CreateTemplate.jsx"
import Intakes from './pages/intakes/Intakes.jsx'
import IntakeDetails from './pages/intakes/IntakeDetails.jsx'
import {BrowserRouter, Route, Routes} from "react-router-dom";

function App() {

  return (
      <BrowserRouter>
          <Routes>
              <Route
                  path="/"
              />

              <Route
                path="intakes"
                element={<Intakes />}
              />

              <Route path="/templates/:id" element={<TemplateDetail />} />
              <Route path="/intakes/:id" element={<IntakeDetails />} />
              <Route path="/intakes/:id/done" element={<IntakeDetails />} />

              <Route
                  path="/auth"
                  element={
                    <AuthPage />
                  }
              />

              <Route
                  path="/templates"
                  element={
                      <TemplatesPage />
                  }
              />

              <Route
                path="/templates/create"
                element={
                  <CreateTemplate />
                }
              />

              <Route
                  path="/logout"
                  element={
                      <Logout />
                  }
              />
          </Routes>
      </BrowserRouter>
  )
}

export default App;
