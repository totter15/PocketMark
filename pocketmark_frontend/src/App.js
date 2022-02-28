import { Routes, Route } from "react-router-dom";
import "./App.css";
import React from "react";
import Regist from "./components/Regist";
import Login from "./components/Login";
import Main from "./components/main/Main";
import Mypage from "./components/Mypage";

function App() {
  return (
    <>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/regist" element={<Regist />} />
        <Route path="/main" element={<Main />} />
        <Route path="/mypage" element={<Mypage />} />
      </Routes>
    </>
  );
}

export default App;
