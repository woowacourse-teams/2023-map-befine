import { DEFAULT_PROD_URL } from '../constants';
import withTokenRefresh from './utils';

export const getApi = async <T>(url: string) => {
  const data = await withTokenRefresh(async () => {
    const apiUrl = `${DEFAULT_PROD_URL + url}`;
    const userToken = localStorage.getItem('userToken');
    const headers: any = {
      'content-type': 'application/json',
    };

    if (userToken) {
      headers.Authorization = `Bearer ${userToken}`;
    }

    const response = await fetch(apiUrl, { method: 'GET', headers });

    if (response.status >= 400) {
      throw new Error('[SERVER] GET 요청에 실패했습니다.');
    }

    const responseData: T = await response.json();
    return responseData;
  });

  return data;
};
