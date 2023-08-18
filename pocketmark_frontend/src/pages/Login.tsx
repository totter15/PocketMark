import React, { useState, useEffect, ChangeEvent } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import './Login.css';
import { login } from '../apis/user';
import client from '../apis/client';

const Login = () => {
	const [checked, setChecked] = useState(false);
	const [input, setInput] = useState({
		email: '',
		pw: '',
	});

	const navigate = useNavigate();

	useEffect(() => {
		localStorage.getItem('autoLogin') === 'true' && navigate('/main');
	}, []);

	const onChange = (e: ChangeEvent<HTMLInputElement>) => {
		const { name, value } = e.target;
		setInput({
			...input,
			[name]: value,
		});
	};

	const onSubmit = async (e: ChangeEvent<HTMLFormElement>) => {
		e.preventDefault();

		const data = {
			email: input.email,
			pw: input.pw,
		};

		if (checked) {
			localStorage.setItem('autoLogin', 'true');
		}

		try {
			const response = await login(data);
			const { tokenBox, itemId } = response.data;
			const { accessToken, refreshToken } = tokenBox;

			if (response.success && accessToken) {
				localStorage.setItem('refreshToken', refreshToken);
				localStorage.setItem('lastId', itemId);
				localStorage.setItem('token', accessToken);

				client.defaults.headers.common[
					'Authorization'
				] = `Bearer ${accessToken}`;
				navigate('main');
			}
		} catch (e) {
			// switch (res.status) {
			// 	case 401:
			// 		alert('이메일정보가 없습니다.');
			// 		break;
			// 	case 400:
			// 		alert('이메일 혹은 비밀번호가 틀립니다!');
			// 		break;
			// 	case 200:
			// 		navigate('/main');
			// }
		}
	};

	return (
		<div className='login'>
			<div className='title'>Pocket Mark</div>
			<form onSubmit={onSubmit}>
				<div className='id'>
					<label>아이디</label>
					<input
						name='email'
						type={'email'}
						value={input.email}
						placeholder='아이디를 입력해주세요.'
						onChange={onChange}
					/>
				</div>
				<div className='password'>
					<label>비밀번호</label>
					<input
						name='pw'
						type={'password'}
						value={input.pw}
						placeholder='비밀번호를 입력해주세요.'
						onChange={onChange}
					/>
				</div>
				<button>로그인</button>
				<div className='autoLogin'>
					<input
						type='checkbox'
						checked={checked}
						onChange={() => {
							setChecked(!checked);
						}}
					/>
					자동 로그인
				</div>
			</form>

			<div className='regist'>
				아직 회원이 아니세요?
				<Link className='link' to='/regist'>
					회원가입 하러가기
				</Link>
			</div>
		</div>
	);
};

export default Login;
