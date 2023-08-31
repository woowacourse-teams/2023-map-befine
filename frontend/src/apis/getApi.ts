// const API_URL =
//   process.env.NODE_ENV === 'production'
//     ? process.env.REACT_APP_API_DEFAULT_PROD
//     : process.env.REACT_APP_API_DEFAULT_DEV;

import { DEFAULT_PROD_URL } from '../constants';

const API_URL =
  process.env.NODE_ENV === 'development'
    ? 'http://localhost:3000'
    : DEFAULT_PROD_URL;

interface Headers {
  'Content-Type': string;
  [key: string]: string;
}
export const getApi = async <T>(
  type: 'tMap' | 'default' | 'login',
  url: string,
): Promise<T> => {
  const apiUrl = type === 'tMap' || type === 'login' ? url : `${API_URL + url}`;

  const userToken = localStorage.getItem('userToken');
  const headers: Headers = {
    'Content-Type': 'application/json',
  };
  if (userToken) {
    headers['Authorization'] = `Bearer ${userToken}`;
  }
  const response = await fetch(apiUrl, {
    method: 'GET',
    headers: headers,
  });

  const responseData: T = await response.json();
  if (response.status >= 400) {
    //todo: status 상태별로 로그인 토큰 유효 검증
    throw new Error('API 요청에 실패했습니다.');
  }
  return responseData;
};
