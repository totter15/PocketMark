import React from "react";
import {Link} from 'react-router-dom';
import Logo from "../../function/Logo";

const style = {
    display:"flex",
    color:"red",
    flexDirection:"column",
    alignItems: "center",
    justifyContent: "center",
    width:"100vw",
    height:"100vh",
    fontSize:"1.2rem",
};

const aStyle={
    
    textDecoration: "none",
    color:"black",
    textTransform: "uppercase",
    fontSize: "1rem",
    textAlign: "center",
    fontWeight: "600",
    margin: "10px",
    
};

const h3 = {
    marginBottom:"40px",
    marginTop:"100px",
};



export default function SignUpSuccess(){
    console.log()
    return (
        <div style={style}>
            <Logo />
            <h3 style={h3}>🥳 회원가입에 성공하셨습니다! 🥳</h3>
            <Link to="/login" style={aStyle}>Login</Link>
            <Link to="/" style={aStyle}>Home</Link>
            <div style={h3}></div>
        </div>

    )
}