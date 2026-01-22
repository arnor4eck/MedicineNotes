import './App.css'
import AuthPage from "./pages/auth/AuthPage.tsx"
import Logout from "./pages/auth/Logout.tsx"
import TemplatesPage from "./pages/templates/TemplatesPage.tsx"
import TemplateDetail from "./pages/templates/TemplateDetail.tsx"
import CreateTemplate from "./pages/templates/CreateTemplate.tsx"
import Intakes from './pages/intakes/Intakes.tsx'
import IntakeDetails from './pages/intakes/IntakeDetails.tsx'
import Main from './pages/Main.tsx'
import {BrowserRouter, Route, Routes} from "react-router-dom";

function App() {

  return (
      <BrowserRouter>
          <Routes>
              <Route
                  path="/"
                  element={<Main />}
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
