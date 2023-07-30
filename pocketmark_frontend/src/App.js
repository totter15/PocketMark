import { Routes, Route } from 'react-router-dom';
import './App.css';
import React from 'react';
import Regist from './pages/Regist';
import RegistOk from './pages/RegistOk';
import Login from './components/Login';
import Main from './components/main/Main';

function App() {
	return (
		<>
			<Routes>
				<Route path='/' element={<Login />} />
				<Route path='/regist' element={<Regist />} />
				<Route path='/registOk' element={<RegistOk />} />
				<Route path='/main' element={<Main />} />
			</Routes>
		</>
	);
}

export default App;
