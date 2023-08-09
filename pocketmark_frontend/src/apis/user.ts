import { EmailCodeCheckRequest, SignUpRequest } from '../interfaces/user';
import { getCookis } from '../lib/cookie';
import client, { noAuthClient } from './client';

export async function getRefreshToken(refreshToken: string) {
	const { data } = await noAuthClient.post('api/v1/refresh-token', {
		refreshToken,
	});
	return data;
}

export async function emailCheck(email: string) {
	const { data } = await noAuthClient.post('api/v1/email-check', { email });
	return data;
}

export async function sendEmailCode(email: string) {
	const { data } = await noAuthClient.post('api/v1/send-authentication-email', {
		email,
	});
	return data;
}

export async function emailCodeCheck({ code, email }: EmailCodeCheckRequest) {
	const { data } = await noAuthClient.post('/api/v1/authentication-email', {
		code,
		email,
	});
	return data;
}

export async function aliasCheck(nickname: string) {
	const { data } = await client.post('api/v1/alias-check', { nickname });
	return data;
}

export async function signUp({ email, nickname, pw }: SignUpRequest) {
	const { data } = await noAuthClient.post('api/v1/sign-up', {
		email,
		nickname,
		pw,
	});
	return data;
}

export async function login({ email, pw }: { email: string; pw: string }) {
	const { data } = await client.post('api/v1/login', { email, pw });
	return data;
}

export async function getToken() {
	const refreshToken = getCookis('refreshToken');
	const { data } = await noAuthClient.post('api/v1/refresh-token', {
		refreshToken,
	});
	return data;
}
