import { Routes, Route, Redirect } from "react-router-dom";
import "./App.css";
import Regist from "./components/Regist";
import Login from "./components/Login";
import Main from "./components/main/Main";

function App() {
  const isAuthorized = false;
  return (
    <>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/regist" element={<Regist />} />
        <Route path="/main" element={<Main />} />
      </Routes>
    </>
  );
}

export default App;
