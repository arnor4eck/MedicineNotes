import './App.css'
import AuthPage from "./pages/auth/AuthPage.jsx"
import Logout from "./pages/auth/Logout.jsx"
import TemplatesPage from "./pages/templates/TemplatesPage.jsx"
import TemplateDetail from "./pages/templates/TemplateDetail.jsx"
import {BrowserRouter, Route, Routes} from "react-router-dom";

function App() {

  return (
      <BrowserRouter>
          <Routes>
              <Route
                  path="/"
                  /*element={
                      isAuthenticated ?
                          <Navigate to="/objects" replace /> :
                          <Navigate to="/auth" replace />
                  }*/
              />

              <Route path="/templates/:id" element={<TemplateDetail />} />

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
