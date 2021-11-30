import React from "react";
import '../css/LoginForm.css';
import axios from 'axios';


const id = document.querySelector(".login-container #id");
const pw = document.querySelector(".login-container #pw");
const hidden = document.querySelector(".hidden");


const loginClick = (props)=>{
    
    // // 이 두개를 서버에서 보내서 확인. DB에서 꺼내나? 쿠키? 세션? 
    console.log(id);
    console.log(pw);
    // console.log(getApiUrl);


    // post 로 일단 보내고 get을 받아야됨????
    // http protocol은 모두 res 를 받을 수 있음 (**** 중요 ****) 
    axios.post(`http://localhost:9090/api/login`,{id:`${id.value}`,pw:`${pw.value}`})
    .then((res)=>{
        // console.log(res);
        if(res.data === true){
            window.location.href = 'http://localhost:3000/';
            console.log(res.data);
        }else{
            hidden.className="state";
        }
    })
    .catch((error)=>{console.log(error);})
    .finally(()=>{});   

    axios.delete('sd1',)
    
};


function LoginForm(){
    return(
        <div className="login-container">
            <input autoComplete="off" type='text' id="id" name="id" placeholder="아이디를 입력해주세요" className="input-id"></input>
            <input autoComplete="off" type='text' id="pw" name="pw" placeholder="비밀번호를 입력해주세요" className="input-pw"></input>
            <p className="hidden">아이디나 비밀번호를 잘못 입력하셨습니다.</p>
            <button className="login-btn" onClick={loginClick}>로그인</button>
        </div>

    );

}

export default LoginForm;