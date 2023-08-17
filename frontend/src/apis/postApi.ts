interface Headers {
  'Content-Type': string;
  [key: string]: string;
}

export const postApi = async (url: string, data?: {}, contentType?: string) => {
  const userToken = localStorage.getItem('userToken');
  const headers: Headers = {
    'Content-Type': `${contentType || 'application/json'}`,
  };

  if (userToken) {
    headers['Authorization'] = `Bearer ${userToken}`;
  }

  const response = await fetch(`${process.env.REACT_APP_API_DEFAULT + url}`, {
    method: 'POST',
    headers: headers,
    body: JSON.stringify(data),
  });

  if (response.status !== 201) {
    //todo: status 상태별로 로그인 토큰 유효 검증
    console.error(response);
    throw new Error('API 요청에 실패했습니다.');
  }

  return response;
};
