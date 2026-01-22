import './App.css'
import AuthPage from "./pages/AuthPage.jsx"
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

              <Route
                  path="/auth"
                  element={
                    <AuthPage />
                  }
              />
          </Routes>
      </BrowserRouter>
  )
}

export default App;
