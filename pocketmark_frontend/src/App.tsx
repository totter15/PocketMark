import { Routes, Route } from 'react-router-dom';
import './App.css';
import React from 'react';
import Regist from './pages/Regist';
import RegistOk from './pages/RegistOk';
import Login from './pages/Login';
import Main from './pages/Main';
import { QueryClient, QueryClientProvider } from 'react-query';
import { Provider } from 'react-redux';
import { store } from '../src/slices/index';

function App() {
	const queryClient = new QueryClient();

	return (
		<Provider store={store}>
			<QueryClientProvider client={queryClient}>
				<Routes>
					<Route path='/' element={<Login />} />
					<Route path='/regist' element={<Regist />} />
					<Route path='/registOk' element={<RegistOk />} />
					<Route path='/main' element={<Main />} />
				</Routes>
			</QueryClientProvider>
		</Provider>
	);
}

export default App;
