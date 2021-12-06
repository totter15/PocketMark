import React, {useState} from "react";
import '../css/LoginForm.css';
import axios from 'axios';
import { useNavigate } from "react-router";
import Home from "./pages/Home";
import Cookies from "universal-cookie";





function LoginForm(){
    const [info, setInfo] = useState({userId:'',userPw:''});
    const [hiddenConfig, setConfig] = useState(true);
    const navigate = useNavigate();
    const cookies = new Cookies();
    let csrfToken = cookies.get('XSRF-TOKEN');
    console.log("befroe req")
    console.log(csrfToken);
    

    const loginClick = (props)=>{
        console.log("#CRSF TOKEN");
        console.log(csrfToken);
        // axios.defaults.headers.common['TESTHEADER']='TESTINNNNNG';
        // axios.defaults.headers.common['Set-Cookie']=`XSRF-TOKEN=${csrfToken};`;
        axios.defaults.xsrfCookieName ='XSRF-TOKEN';
        axios.defaults.xsrfHeaderName ='X-XSRF-TOKEN';
        axios.post(`http://localhost:9090/api/login`,info)
        .then((res)=>{
            console.log(res.headers);
            if(res.data === true){
                navigate('/',{state:{isLogin:true}});
            }else{
                setConfig(false);
            }
        })
        .catch((error)=>{console.log(error);})
        .finally(()=>{});  
    };

    const onChange = (e)=>{
        const {name, value} = e.target;
        const nextInputs={
            ...info,
            [name] :value,
        }
        setInfo(nextInputs);
    };


    return(
        <div className="login-container">
            <input autoComplete="off" type='text' id="id" onChange={onChange} name="userId" placeholder="아이디를 입력해주세요" className="input-id"></input>
            <input autoComplete="off" type='text' id="pw" onChange={onChange} name="userPw" placeholder="비밀번호를 입력해주세요" className="input-pw"></input>
            {!hiddenConfig&&<p className="state">아이디나 비밀번호를 잘못 입력하셨습니다.</p>}
            <button className="login-btn" onClick={loginClick}>로그인</button>
        </div>

    );

}

export default LoginForm;