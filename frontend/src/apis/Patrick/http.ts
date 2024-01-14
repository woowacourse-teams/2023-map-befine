import axios, { AxiosInstance, AxiosRequestConfig } from 'axios';

const API_PREFIX = 'api';

export const BASE_URL =
  process.env.APP_URL || `https://mapbefine.com/${API_PREFIX}`;

const axiosInstance = axios.create({
  baseURL: BASE_URL,
  timeout: 5000,
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

// axios.interceptors.request.use()
axios.interceptors.response.use((res) => res.data)
