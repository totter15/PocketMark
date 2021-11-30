import React from "react";
const style = {
display:"flex",
color:"red",
flexDirection:"column",
alignItems: "center",
justifyContent: "center",
width:"100vw",
height:"100vh",
fontSize:"24px",
};
export default function NotFound(){
    return (
    <div style={style}>
        <p>페이지를 찾을 수 없습니다.</p>
        <p>잘못된 경로로 진입하셨어요 😒</p>
    </div>
    );
}