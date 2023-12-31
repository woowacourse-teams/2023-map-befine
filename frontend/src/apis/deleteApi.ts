import { DEFAULT_PROD_URL } from '../constants';
import { ContentTypeType } from '../types/Api';
import withTokenRefresh from './utils';

interface Headers {
  'content-type': string;
  [key: string]: string;
}

export const deleteApi = async (url: string, contentType?: ContentTypeType) => {
  const data = await withTokenRefresh(async () => {
    const apiUrl = `${DEFAULT_PROD_URL + url}`;
    const userToken = localStorage.getItem('userToken');
    const headers: Headers = {
      'content-type': 'application/json',
    };

    if (userToken) {
      headers.Authorization = `Bearer ${userToken}`;
    }

    if (contentType) {
      headers['content-type'] = contentType;
    }

    const response = await fetch(apiUrl, {
      method: 'DELETE',
      headers,
    });

    if (response.status >= 400) {
      throw new Error('[SERVER] DELETE 요청에 실패했습니다.');
    }
  });

  return data;
};
