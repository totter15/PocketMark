import React from 'react';
import { Link } from 'react-router-dom';
import './RegistOk.css';

const RegistOk = () => {
	return (
		<div className='registOkContainer'>
			<h2>회원가입 완료!</h2>
			<Link to={'/'}>
				<div>로그인하러 가기</div>
			</Link>
		</div>
	);
};

export default RegistOk;
