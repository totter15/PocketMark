import React, { ChangeEvent, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import {
	aliasCheck,
	emailCheck,
	sendEmailCode,
	signUp,
	emailCodeCheck,
} from '../apis/user';
import './Regist.css';

const Regist = () => {
	const navigate = useNavigate();
	// textValue / check 끼리 객체로 묶어서 정리하기...
	const [email, setEmail] = useState('');
	const [emailOk, setEmailOk] = useState(true);
	const [emailSend, setEmailSend] = useState(false);
	const [password, setPassword] = useState('');
	const [passwordCheck, setPasswordCheck] = useState('');
	const [check, setCheck] = useState(false); //비밀번호 규칙
	const [passwordSame, setPasswordSame] = useState(false); //비밀번호 확인
	const [nickname, setNickname] = useState('');
	const [nicknameOk, setNicknameOk] = useState(true);
	const [nicknameMessage, setNicknameMessage] = useState('');
	const [emailMessage, setEmailMessage] = useState('');

	const [emailCode, setEmailCode] = useState('');
	const [isEmailCodeCheck, setIsEmailCodeCheck] = useState(false);
	const [emailCodeChecked, setEmailCodeChecked] = useState(false);

	const reg = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/; //최소 8자이상 영문,숫자포함

	async function onSubmit() {
		const data = {
			email,
			pw: password,
			nickname,
		};

		const response = await signUp(data);
		response.success && navigate('/registOk');
	}

	async function onNicknameCheck() {
		const data = await aliasCheck(nickname);
		const { available } = data.data;

		if (data.success) {
			setNicknameOk(available);
			setNicknameMessage(
				available ? '사용 가능한 닉네임입니다.' : '사용 불가한 닉네임입니다.'
			);
		} else {
			setNicknameOk(false);
			setNicknameMessage('닉네임 형식을 확인해 주세요');
		}
	}

	async function onEmailCheck() {
		const data = await emailCheck(email);
		const { available } = data?.data;

		if (data.success) {
			setEmailOk(available);
			setEmailSend(available);
			setEmailMessage(
				available ? '사용 가능한 이메일 입니다.' : '사용 불가한 이메일 입니다.'
			);
			available && sendEmailCode(email);
		} else {
			setEmailOk(false);
			setEmailMessage('이메일 형식을 확인해 주세요');
		}
	}

	async function onEmailCodeCheck() {
		const data = await emailCodeCheck({ email, code: emailCode });
		if (data.success) {
			setIsEmailCodeCheck(data.data.success);
			setEmailCodeChecked(true);
		}
	}

	//비밀번호 규칙 확인
	const onCheck = (e: ChangeEvent<HTMLInputElement>) => {
		setPassword(e.target.value);
		setCheck(reg.test(e.target.value));
	};

	// 비밀번호 확인
	const onPasswordSame = (e: ChangeEvent<HTMLInputElement>) => {
		setPasswordCheck(e.target.value);
		setPasswordSame(e.target.value === password);
	};

	return (
		<div className='registContainer'>
			<Link to='/'>
				<h1>Pocket Mark</h1>
			</Link>

			<h3 className='title'>회원가입</h3>
			<form onSubmit={(e) => e.preventDefault()}>
				<div className={emailOk ? 'Ok' : 'email'}>
					<label>이메일</label>
					<div className='check'>
						<input
							type={'email'}
							value={email}
							id='email'
							onChange={(e) => {
								setEmail(e.target.value);
								setEmailMessage('');
							}}
							placeholder='이메일을 입력해주세요'
						/>
						{!emailSend && (
							<button onClick={onEmailCheck} className='checkButton'>
								중복 확인
							</button>
						)}
					</div>
					<p
						className='message'
						style={{ color: emailOk ? 'rgb(87, 92, 128)' : 'orangered' }}
					>
						{emailMessage}
					</p>
				</div>

				{emailSend && (
					<div className={'emailCode'}>
						<div className='check'>
							<input
								value={emailCode}
								id='email'
								onChange={(e) => {
									setEmailCode(e.target.value);
								}}
								placeholder='이메일에 적힌 코드를 입력해주세요'
							/>
							<button onClick={onEmailCodeCheck} className='checkButton'>
								이메일 인증 하기
							</button>
						</div>
						<p
							className='message'
							style={{
								color: isEmailCodeCheck ? 'rgb(87, 92, 128)' : 'orangered',
							}}
						>
							{emailCodeChecked
								? isEmailCodeCheck
									? '이메일 인증이 완료되었습니다.'
									: '인증코드가 맞지않습니다.'
								: ''}
						</p>
					</div>
				)}

				<div className={check ? 'passwordOk' : 'password'}>
					<label>
						비밀번호 <p>8자 이상 영문, 숫자 조합</p>
					</label>
					<input
						type={'password'}
						value={password}
						id='password'
						onChange={onCheck}
						placeholder='비밀번호를 입력해주세요'
					/>
				</div>

				<div className={passwordSame ? 'passwordOk' : 'password'}>
					<label>비밀번호 확인</label>
					<input
						type={'password'}
						value={passwordCheck}
						id='passwordCheck'
						onChange={onPasswordSame}
						placeholder='비밀번호를 한번 더 입력해주세요'
					/>
				</div>

				<div className={nicknameOk ? 'Ok' : 'nickname'}>
					<label>
						닉네임
						<p>2-12자리</p>
					</label>
					<div className={'check'}>
						<input
							type={'text'}
							value={nickname}
							id='nickname'
							onChange={(e) => {
								setNickname(e.target.value);
								setNicknameMessage('');
							}}
							placeholder='닉네임을 입력해주세요'
						/>
						<button className='checkButton' onClick={onNicknameCheck}>
							중복확인
						</button>
					</div>
					<p
						className='message'
						style={{ color: nicknameOk ? 'rgb(87, 92, 128)' : 'orangered' }}
					>
						{nicknameMessage}
					</p>
				</div>

				<button onClick={onSubmit}>회원가입</button>
			</form>
		</div>
	);
};

export default Regist;
