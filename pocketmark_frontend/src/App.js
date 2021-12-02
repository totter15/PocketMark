
import LoginForm from './components/LoginForm';
import Navigator from './components/Navigator';
import {Route, Router ,Routes,HashRouter, BrowserRouter, Link} from "react-router-dom";
import Home from './components/pages/Home';
import Example from './components/pages/Example';
import { useCallback } from 'react';
import ErrorPage from './components/pages/ErrorPage';
import {ErrorBoundary} from "react-error-boundary";
import NotFound from './components/pages/NotFound';
import SignUp from './components/pages/SignUp';
import SignUpSuccess from './components/pages/SignUpSuccess';

function App() {
  
  return (
    <div>
      <ErrorBoundary>

      <BrowserRouter>
        <Navigator/>
        
        <Routes>
          <Route path="/" exact element={<Home/>} /> 
          <Route path="/example" exact element={<Example/>} />
          <Route path="/login" exact element={<LoginForm/>} /> 
          <Route path="/sign-up" exact element={<SignUp/>} /> 
          <Route path="/sign-up/success" exact element={<SignUpSuccess/>} /> 
          <Route path="*" element={<NotFound/>}/>
        </Routes>
        
      </BrowserRouter>
      </ErrorBoundary>
  

      
    </div>
  );
}

export default App;
