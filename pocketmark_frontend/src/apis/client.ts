import axios, { AxiosResponse, InternalAxiosRequestConfig } from 'axios';
import { getCookis } from '../utils/cookie';
import { getToken } from './user';

const client = axios.create({
	baseURL: 'http://localhost:9090/',
});

export const noAuthClient = axios.create({
	baseURL: 'http://localhost:9090/',
});

// client.interceptors.request.use(function (
// 	config: InternalAxiosRequestConfig
// ): InternalAxiosRequestConfig {
// 	const token = getCookis('refresh-token');
// 	if (token) {
// 		config.headers.Authorization = `Bearer ${token}`;
// 	}
// 	return config;
// });

client.interceptors.response.use(
	function (response: AxiosResponse): AxiosResponse {
		return response;
	},
	async function ({ config, response }) {
		if (response.status === 403) {
			const { data } = await getToken();
			const { accessToken } = data ?? {};
			client.defaults.headers.common['Authorization'] = `Bearer ${accessToken}`;
		}
		// 요청 오류가 있는 작업 수행
		return axios(config);
	}
);

export default client;
