import React from "react";
import { Link, Route } from "react-router-dom";
import '../css/Navigator.css';


function Navigator(){
    // home('/')으로 이동시, 인증에따라 로그인 됬을경우, 아닐경우로 렌더링 다르게 해야함 
    return (
        <div className="nav">
            <Link to="/">Go Home</Link>
            <Link to="/recentWork">Go To RecentWork</Link>
        </div>
    ); 
}

export default Navigator;