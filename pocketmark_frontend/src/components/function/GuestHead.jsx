import React from "react";
import { Link } from "react-router-dom";
import '../../css/GuestHead.css';

export default function GuestHead(){
    return (
        <div className="head-container">
            <Link to="login">Login</Link>
            <Link to="sign-up">회원가입</Link>
        </div>
    );
}