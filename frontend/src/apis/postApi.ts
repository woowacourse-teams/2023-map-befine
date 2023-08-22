// const API_URL =
//   process.env.NODE_ENV === 'production'
//     ? process.env.REACT_APP_API_DEFAULT_PROD
//     : process.env.REACT_APP_API_DEFAULT_DEV;

import { DEFAULT_PROD_URL } from '../constants';
import { ContentTypeType } from '../types/Api';

interface Headers {
  'content-type': string;
  [key: string]: string;
}

export const postApi = async (
  url: string,
  data: {},
  contentType?: ContentTypeType,
) => {
  const apiUrl = `${DEFAULT_PROD_URL + url}`;
  const userToken = localStorage.getItem('userToken');
  const headers: Headers = {
    'content-type': 'application/json',
  };

  if (userToken) {
    headers['Authorization'] = `Bearer ${userToken}`;
  }

  if (contentType) {
    headers['content-type'] = contentType;
  }

  const response = await fetch(apiUrl, {
    method: 'POST',
    headers,
    body: JSON.stringify(data),
  });

  if (response.status >= 400) {
    throw new Error('[SERVER] POST 요청에 실패했습니다.');
  }

  return response;
};
