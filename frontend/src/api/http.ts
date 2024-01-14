import axios, { AxiosInstance, AxiosRequestConfig } from 'axios';

const API_POSTFIX = 'api';
const BASE_URL = process.env.APP_URL || `https://mapbefine.com/${API_POSTFIX}`;

const axiosInstance = axios.create({
  baseURL: BASE_URL,
  headers: {
    Authorization: `Bearer ${localStorage.getItem('userToken')}`,
  },
});

export interface HttpClient extends AxiosInstance {
  get<T = unknown>(url: string, config?: AxiosRequestConfig): Promise<T>;
  post<T = unknown>(
    url: string,
    data?: any,
    config?: AxiosRequestConfig,
  ): Promise<T>;
  patch<T = unknown>(
    url: string,
    data?: any,
    config?: AxiosRequestConfig,
  ): Promise<T>;
  put<T = unknown>(
    url: string,
    data?: any,
    config?: AxiosRequestConfig,
  ): Promise<T>;
  delete<T = unknown>(url: string, config?: AxiosRequestConfig): Promise<T>;
}

export const http: HttpClient = axiosInstance;

http.interceptors.response.use((res) => res.data);
