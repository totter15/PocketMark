import React, {useState} from "react";
import '../css/LoginForm.css';
import axios from 'axios';
import { useNavigate } from "react-router";
import Home from "./pages/Home";



function LoginForm(){
    const [info, setInfo] = useState({userId:'',userPw:''});
    const [hiddenConfig, setConfig] = useState(true);
    const navigate = useNavigate();

    const loginClick = (props)=>{
     
        axios.post(`http://localhost:9090/api/login`,info)
        .then((res)=>{
            console.log(info);
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