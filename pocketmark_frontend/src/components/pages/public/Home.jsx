import axios from "axios";
import {Link} from "react-router-dom";
import '../../../css/Home.css';
import GuestHead from "../../function/GuestHead";
import LoginHead from "../../function/LoginHead";
import Logo from "../../function/Logo";
import { useLocation } from "react-router";
import { useNavigate } from "react-router";




export default function Home(props){
    const {state} = useLocation();
    const navigate = useNavigate();
    console.log(state);
    let isLogin = false;
    let jwt = "";

    
    const headerTest = () =>{
        console.log("headerTest");
        console.log(state.jwt)
        let config={
            headers:{
                Authorization : "Bearer "+state.jwt,
            }
        }
        axios.defaults.headers.common['Authorization']= `Bearer ${state.jwt}`;
        
        axios.get("http://localhost:9090/api/header-test")
        .then((res)=>{
            console.log(res);
    
        } 
        )
        .catch().finally();
    };

    if(state!==null){
        if(state.isLogin!==undefined){
            isLogin = state.isLogin;
        }
        if(state.jwt!==undefined){
            console.log("Home");
            console.log(state.jwt);
            jwt=state.jwt;
        }
    }

    const privatePage =()=>{
        navigate('/private',{state:{isLogin, jwt}});
    }
    console.log("JWT : "+jwt);


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
                {/* {(isLogin)?
                    <Link to="/private">Go To Private Page</Link>:null
                } */}
                <button id="test2" onClick={privatePage}>Go TO PrivatePage</button>

            </div>

            
        </div>
    
    );
}