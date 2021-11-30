import React from "react";
import '../../css/SignIn.css';
import axios from 'axios';

const hidden = document.querySelectorAll(".hidden");
const id = document.querySelector("#id");
const email = document.querySelector("#email");

const click = ()=>{
    console.log(id.value);
    console.log(email.value);
    axios.get(`http://localhost:9090/api/sign-in?id=${id.value}&email=${email.value}`)
    .then((res)=>{
        console.log(res);
    })
    .catch((error)=>{console.log(error);})
    .finally(()=>{});




    
};

export default function SignIn(){
    return(
        <div className="sign-container">
            <h3 className="sign-desc">아이디</h3>
            <input autoComplete="off" type='text' id="id" name="id" placeholder="사용하실 아이디를 입력해주세요" className="sign-input"></input>
            <p className="hidden" id="id-check">중복된 아이디입니다.</p>
            <h3 className="sign-desc">비밀번호</h3>
            <input autoComplete="off" type='text'  name="pw" placeholder="사용하실 비밀번호를 입력해주세요" className="sign-input"></input>
            <h3 className="sign-desc">이메일</h3>
            <input autoComplete="off" type='text' id="email" name="email" placeholder="사용하실 이메일을 입력해주세요" className="sign-input"></input>
            <p className="hidden" id="email-check">중복된 이메일입니다.</p>
            <button className="sign-btn" onClick={click}>완료</button>
        </div>
        
    );
}