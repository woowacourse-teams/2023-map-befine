import { DEFAULT_PROD_URL } from '../constants';
import { ContentTypeType } from '../types/Api';
import withTokenRefresh from './utils';

export const patchApi = async (
  url: string,
  payload: {},
  contentType?: ContentTypeType,
) => {
  const data = await withTokenRefresh(async () => {
    const apiUrl = `${DEFAULT_PROD_URL + url}`;
    const userToken = localStorage.getItem('userToken');
    const headers: any = {
      'content-type': 'application/json',
    };

    if (userToken) {
      headers.Authorization = `Bearer ${userToken}`;
    }

    if (contentType) {
      headers['content-type'] = contentType;
    }

    const response = await fetch(apiUrl, {
      method: 'PATCH',
      headers,
      body: JSON.stringify(payload),
    });

    if (response.status >= 400) {
      throw new Error('[SERVER] PUT 요청에 실패했습니다.');
    }

    return response;
  });

  return data;
};
