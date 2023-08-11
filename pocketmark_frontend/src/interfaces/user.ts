export interface EmailCodeCheckRequest {
	code: string;
	email: string;
}

export interface SignUpRequest {
	email: string;
	nickname: string;
	pw: string;
}
