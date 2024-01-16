import axios, {
  AxiosInstance,
  AxiosRequestConfig,
  AxiosRequestHeaders,
} from 'axios';

const API_POSTFIX = 'api';
const BASE_URL = process.env.APP_URL || `https://mapbefine.com/${API_POSTFIX}`;
const axiosInstance = axios.create({
  baseURL: BASE_URL,
  headers: { Authorization: `Bearer ${localStorage.getItem('userToken')}` },
});

let refreshResponse: Promise<Response> | null = null;

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
http.interceptors.request.use(
  async (config) => {
    const userToken = localStorage.getItem('userToken');

    if (userToken && isTokenExpired(userToken)) {
      await updateToken(config.headers);
    }
    return config;
  },
  (error) => Promise.reject(error),
);

const isTokenExpired = (token: string) => {
  const decodedPayloadObject = decodeToken(token);
  return decodedPayloadObject.exp * 1000 < Date.now();
};

const decodeToken = (token: string) => {
  const tokenParts = token.split('.');

  if (tokenParts.length !== 3) {
    throw new Error('토큰이 잘못되었습니다.');
  }

  const decodedPayloadString = atob(tokenParts[1]);

  return JSON.parse(decodedPayloadString);
};

async function updateToken(headers: AxiosRequestHeaders) {
  const response = await refreshToken(headers);
  const responseCloned = response.clone();
  const newToken = await responseCloned.json();

  localStorage.setItem('userToken', newToken.accessToken);
}

async function refreshToken(headers: AxiosRequestHeaders): Promise<Response> {
  if (refreshResponse !== null) {
    return refreshResponse;
  }

  const accessToken = localStorage.getItem('userToken');
  refreshResponse = fetch(`${BASE_URL}/refresh-token`, {
    method: 'POST',
    headers,
    body: JSON.stringify({
      accessToken,
    }),
  });

  const responseData = await refreshResponse;
  refreshResponse = null;

  if (!responseData.ok) {
    throw new Error('Failed to refresh access token.');
  }

  return responseData;
}
