const decodeToken = (token: string) => {
  const tokenParts = token.split('.');
  if (tokenParts.length !== 3) {
    throw new Error('토큰이 잘못되었습니다.');
  }

  const decodedPayloadString = atob(tokenParts[1]);
  console.log('decodedPayloadString', decodedPayloadString);
  return JSON.parse(decodedPayloadString);
};

async function refreshToken(headers: Headers): Promise<string> {
  console.log('L12 refreshTOken이 호출은 되었는지 확인', headers);
  const accessToken = localStorage.getItem('userToken');
  console.log('getAPI Line 14', accessToken);
  try {
    // 서버에 새로운 엑세스 토큰을 요청하기 위한 네트워크 요청을 보냅니다.
    const refreshResponse = await fetch(`${DEFAULT_PROD_URL}/refresh-token`, {
      method: 'POST',
      headers,
      body: JSON.stringify({
        accessToken: accessToken,
      }),
    });

    // 서버 응답이 성공적인지 확인합니다.
    if (!refreshResponse.ok) {
      console.log('refreshResponse.ok하지 못함');
      throw new Error('Failed to refresh access token.');
    }

    console.log('refreshResponse', refreshResponse);
    console.log('refreshResponse.text()', refreshResponse.text());
    // 새로운 엑세스 토큰을 반환합니다.
    return await refreshResponse.text();
  } catch (error) {
    // 네트워크 요청 실패 또는 예외 발생 시 예외를 캐치하여 처리합니다.
    console.error('네트워크 요청 실패 또는 예외 발생:', error);
    throw error; // 예외를 다시 throw하여 상위 코드로 전파합니다.
  }
}

async function withTokenRefresh<T>(callback: () => Promise<T>): Promise<T> {
  let userToken = localStorage.getItem('userToken');
  console.log('userToken', userToken);
  if (userToken) {
    const decodedPayloadObject = decodeToken(userToken);
    console.log('decodedPayloadObject', decodedPayloadObject);
    // decodeToken의 결과로 만료 여부 판단
    if (decodedPayloadObject.exp * 1000 < Date.now()) {
      console.log('AccessToken 만료되어 재요청합니다');

      const headers: any = {
        'content-type': 'application/json',
        Authorization: `Bearer ${userToken}`,
      };
      console.log(`Authorization : Bearer ${userToken}`);
      //새로운 토큰 재발급
      userToken = await refreshToken(headers);
      console.log('L59 userToken', userToken);
      localStorage.setItem('userToken', userToken);
      console.log('localStorage에 새로운 토큰 적용 성공!');
    }
  }

  //인자로 넘겨준 실제 fetch함수 실행
  console.log('callback 실행');
  return callback();
}

import { DEFAULT_PROD_URL } from '../constants';

const API_URL =
  process.env.NODE_ENV === 'development'
    ? 'http://localhost:3000'
    : DEFAULT_PROD_URL;

interface Headers {
  'content-type': string;
  [key: string]: string;
}

export const getApi = async <T>(url: string) => {
  return await withTokenRefresh(async () => {
    const apiUrl = `${DEFAULT_PROD_URL + url}`;
    const userToken = localStorage.getItem('userToken');
    const headers: any = {
      'content-type': 'application/json',
    };

    if (userToken) {
      headers['Authorization'] = `Bearer ${userToken}`;
    }

    console.log('response', DEFAULT_PROD_URL + url);

    const response = await fetch(apiUrl, { method: 'GET', headers });

    console.log(response);

    if (response.status >= 400) {
      throw new Error('[SERVER] GET 요청에 실패했습니다.');
    }

    const responseData: T = await response.json();
    console.log('responseData L84까지 성공', responseData);
    return responseData;
  });
};
