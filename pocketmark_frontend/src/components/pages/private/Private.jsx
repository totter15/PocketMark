import React from "react";
import { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router";
import { useLocation } from "react-router";


export default function Private(){
    
    const {state} = useLocation();
    const navigate = useNavigate();
    console.log(state);

    const requestToServer =() =>{
        let config={
            headers:{
                Authorization : "Bearer "+state.jwt,
            }
        }
        axios.defaults.headers.common['Authorization']= `Bearer ${state.jwt}`;
        axios.interceptors.request.use(res=>{
            console.log(res);
            return res;
        }, error =>{
            console.log(error);
        });
        
        axios.get("http://localhost:9090/api/private")
        .then((res)=>{
            console.log(res);
        } 
        )
        .catch( (e) =>{console.log(e);}
        ).finally();
    }
    
    return (<div>
        <h1>this is Private Page</h1>
        <button onClick={requestToServer}>Get Resource</button>
    
    </div>);
}