import { DEFAULT_PROD_URL } from '../constants';

let refreshResponse: Promise<Response> | null = null;

const decodeToken = (token: string) => {
  const tokenParts = token.split('.');
  if (tokenParts.length !== 3) {
    throw new Error('토큰이 잘못되었습니다.');
  }

  const decodedPayloadString = atob(tokenParts[1]);

  return JSON.parse(decodedPayloadString);
};

async function refreshToken(headers: Headers): Promise<Response> {
  if (refreshResponse !== null) {
    return refreshResponse;
  }

  const accessToken = localStorage.getItem('userToken');
  try {
    // 서버에 새로운 엑세스 토큰을 요청하기 위한 네트워크 요청을 보냅니다.
    refreshResponse = fetch(`${DEFAULT_PROD_URL}/refresh-token`, {
      method: 'POST',
      headers,
      body: JSON.stringify({
        accessToken: accessToken,
      }),
    });

    const responseData = await refreshResponse;
    refreshResponse = null;

    // 서버 응답이 성공적인지 확인합니다.
    if (!responseData.ok) {
      throw new Error('Failed to refresh access token.');
    }

    // 새로운 엑세스 토큰을 반환합니다.
    return responseData;
  } catch (error) {
    // 네트워크 요청 실패 또는 예외 발생 시 예외를 캐치하여 처리합니다.
    console.error('네트워크 요청 실패 또는 예외 발생:', error);
    throw error; // 예외를 다시 throw하여 상위 코드로 전파합니다.
  }
}

const isTokenExpired = (token: string) => {
  const decodedPayloadObject = decodeToken(token);
  return decodedPayloadObject.exp * 1000 < Date.now();
};

async function updateToken(headers: Headers) {
  const response = await refreshToken(headers);
  const responseCloned = response.clone();

  try {
    const newToken = await responseCloned.json();

    localStorage.setItem('userToken', newToken.accessToken);
  } catch (e) {
    console.error(e);

    return;
  }
}

export default async function withTokenRefresh<T>(
  callback: () => Promise<T>,
): Promise<T> {
  const userToken = localStorage.getItem('userToken');

  if (userToken && isTokenExpired(userToken)) {
    const headers: any = {
      'content-type': 'application/json',
      Authorization: `Bearer ${userToken}`,
    };

    await updateToken(headers);
  }

  return callback();
}
