import { Routes, Route } from "react-router-dom";
import "./App.css";
import React, { useEffect, useState } from "react";
import Regist from "./components/Regist";
import Login from "./components/Login";
import Main from "./components/main/Main";
import { getCookis } from "./lib/cookie";

function App() {
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
