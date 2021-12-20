import React, {useState} from "react";
import '../../../css/SignUp.css';
import axios from 'axios';
import { useNavigate } from "react-router";




export default function SignIn(props){
    const [info, setInfo] = useState({userId:'',userPw:'',userEmail:'',code:''});
    const navigate = useNavigate(); /// fu...xk...
    let send = false; let check = false;
    const click = (e)=>{
        // console.log(info);
        axios.post(`http://localhost:9090/api/sign-up`,info)
        .then((res)=>{
            console.log(res);
            if(res.data === true){
                navigate("/sign-up/success");
            }
        })
        .catch((error)=>{console.log(error);})
        .finally(()=>{});
    };

    const sendEmail = () =>{
        axios.post(`http://localhost:9090/api/sign-up/email-send`,info.userEmail)
        .then((res)=>{
            console.log(res);
            if(res.data === "SEND"){
                send= true;
            }
        })
        .catch().finally();
    }
    const checkCode = () =>{
        axios.post(`http://localhost:9090/api/sign-up/email-check`,info)
        .then((res)=>{
            console.log(res);
            if(res.data === "OK"){
                send= true;
                check=true;
                console.log("check : " + check);
            }
        })
        .catch().finally();
    }


    const onChange = (e)=>{
        const {name, value} = e.target;
        const nextInputs={
            ...info,
            [name] :value,
        }
        setInfo(nextInputs);
    };

    return(
        <div className="sign-container">
            <h3 className="sign-desc">아이디</h3>
            <input autoComplete="off" type='text' onChange={onChange} id="id" name="userId" placeholder="사용하실 아이디를 입력해주세요" className="sign-input"></input>
            <p className="hidden" id="id-check">중복된 아이디입니다.</p>
            <div className="sign-desc">
                <h3>비밀번호</h3>
                {send
                ?(<div><input autoComplete="off" type='text' onChange={onChange} id="code" name="code" placeholder="인증코드를 입력해주세요" className="sign-input"></input>
                 <button className="verify">전송</button></div>)
                :<button className="verify" onClick={sendEmail}>인증하기</button>}
            </div>
            <input autoComplete="off" type='text' onChange={onChange} id="pw"  name="userPw" placeholder="사용하실 비밀번호를 입력해주세요" className="sign-input"></input>
            <h3 className="sign-desc">이메일</h3>
            <input autoComplete="off" type='text' onChange={onChange} id="email" name="userEmail" placeholder="사용하실 이메일을 입력해주세요" className="sign-input"></input>
            <p className="hidden" id="email-check">중복된 이메일입니다.</p>
            <button className="sign-btn" onClick={click}>완료</button>
        </div>
        
    );
}