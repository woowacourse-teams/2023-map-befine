interface Headers {
  'Content-Type': string;
  [key: string]: string;
}

const API_URL =
  process.env.NODE_ENV === 'production'
    ? process.env.REACT_APP_API_DEFAULT_PROD
    : process.env.REACT_APP_API_DEFAULT_DEV;

export const postApi = async (url: string, data?: {}, contentType?: string) => {
  const userToken = localStorage.getItem('userToken');
  const headers: Headers = {
    'Content-Type': `${contentType || 'application/json'}`,
  };

  if (userToken) {
    headers['Authorization'] = `Bearer ${userToken}`;
  }

  const response = await fetch(`${API_URL + url}`, {
    method: 'POST',
    headers: headers,
    body: JSON.stringify(data),
  });

  if (response.status >= 400) {
    //todo: status 상태별로 로그인 토큰 유효 검증
    throw new Error('API 요청에 실패했습니다.');
  }

  return response;
};
