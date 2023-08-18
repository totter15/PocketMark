import { Routes, Route } from 'react-router-dom';
import './App.css';
import React, { Suspense, lazy } from 'react';
import { QueryClient, QueryClientProvider } from 'react-query';
import { Provider } from 'react-redux';
import { store } from '../src/slices/index';

const Login = lazy(() => import('./pages/Login'));
const Regist = lazy(() => import('./pages/Regist'));
const RegistOk = lazy(() => import('./pages/RegistOk'));
const Main = lazy(() => import('./pages/Main'));

function App() {
	const queryClient = new QueryClient();

	return (
		<Suspense fallback={<div>로딩중...</div>}>
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
		</Suspense>
	);
}

export default App;
