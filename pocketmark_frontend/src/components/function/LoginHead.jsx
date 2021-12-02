import React from "react";  
import { useNavigate } from "react-router";

const style={
    color:"black"
};

export default function LoginHead(){
    
    const navigate = useNavigate();
    const logOut= ()=>{
        navigate('/',{state: {isLogin:false}})
    }
    return(
        <div style={style}>
            Login Success..!
            <button onClick={logOut}>LogOut</button>
        </div>
    );
}