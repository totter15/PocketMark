import axios, { AxiosResponse, InternalAxiosRequestConfig } from 'axios';
import { getCookis } from '../lib/cookie';

const client = axios.create({
	baseURL: 'http://localhost:9090/',
});

client.interceptors.request.use(function (
	config: InternalAxiosRequestConfig
): InternalAxiosRequestConfig {
	const token = getCookis('refresh-token');
	if (token) {
		config.headers.Authorization = `Bearer ${token}`;
	}
	return config;
});

client.interceptors.response.use(
	function (response: AxiosResponse): AxiosResponse {
		return response;
	},
	function ({ config, response }) {
		if (response.status === 403) {
			// TODO : 새 토큰 요청
		}
		// 요청 오류가 있는 작업 수행
		return axios(config);
	}
);

export default client;
