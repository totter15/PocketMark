import axios from "axios";
import {Link} from "react-router-dom";
import '../../css/Home.css';
import GuestHead from "../function/GuestHead";
import LoginHead from "../function/LoginHead";
import Logo from "../function/Logo";
import { useLocation } from "react-router";


const headerTest = () =>{
    axios.get("http://localhost:9090/api/header-test")
    .then((res)=>console.log(res))
    .catch().finally();
};


export default function Home(props){
    const {state} = useLocation();
    console.log(state);
    let isLogin = false;
    if(state!==null){
        if(state.isLogin!==undefined){
            isLogin = state.isLogin;
        }
    }


    return (
        <div id="container">

            <div id='first-section'>
                {isLogin? <LoginHead/>:<GuestHead/>}
            </div>
            <div id='second-section'>
                <Logo/>
            </div>
            <div id='third-section'>
                <div id="description">
                    <p>북마크를 모바일과 웹에서 저장하세요!</p>
                    <p>언제든지 친구에게 공유해보세요 🤗</p>
                </div>
                <button id="test" onClick={headerTest}>HTTP HeaderTest</button>

            </div>

            
        </div>
    
    );
}