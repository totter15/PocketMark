import {Link} from "react-router-dom";
import '../../css/Home.css';

export default function Home(){
    return (
        <div id="container">

            <div id='first-section'>
                <Link to="login">Login</Link>
                <Link to="sign-in">회원가입</Link>
            </div>
            <div id='second-section'>
                <div id="app-name">
                    <div id="blank">
                    </div>
                    <p id='name-b'>P</p><p id='name-y'>O</p><p id='name-g'>R</p><p id='name-r'>T</p>
                    <p id='name-b'>A</p><p id='name-y'>B</p><p id='name-g'>L</p><p id='name-r'>E</p>
                    <p id='name-b'>M</p><p id='name-y'>A</p><p id='name-g'>R</p><p id='name-r'>K</p>
                    
                    <div id="blank">
                    </div>
                </div>

            </div>
            <div id='third-section'>
                <div id="description">
                    <p>북마크를 모바일과 웹에서 저장하세요!</p>
                    <p>언제든지 친구에게 공유해보세요 🤗</p>
                </div>

            </div>

            
        </div>
    
    );
}