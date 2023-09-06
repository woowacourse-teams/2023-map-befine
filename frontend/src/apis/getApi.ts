// const API_URL =
//   process.env.NODE_ENV === 'production'
//     ? process.env.REACT_APP_API_DEFAULT_PROD
//     : process.env.REACT_APP_API_DEFAULT_DEV;

import { DEFAULT_PROD_URL } from '../constants';

interface Headers {
  'content-type': string;
  [key: string]: string;
}

export const getApi = async <T>(url: string) => {
  const apiUrl = `${DEFAULT_PROD_URL + url}`;
  const userToken = localStorage.getItem('userToken');
  const headers: Headers = {
    'content-type': 'application/json',
  };

  if (userToken) {
    headers['Authorization'] = `Bearer ${userToken}`;
  }

  const response = await fetch(apiUrl, {
    method: 'GET',
    headers,
  });

  if (response.status >= 400) {
    throw new Error('[SERVER] GET 요청에 실패했습니다.');
  }

  const responseData: T = await response.json();

  return responseData;
};
