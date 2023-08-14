export const getApi = async <T>(
  type: 'tMap' | 'default' | 'login',
  url: string,
): Promise<T> => {
  const apiUrl =
    type === 'tMap' || type === 'login'
      ? url
      : `${process.env.REACT_APP_API_DEFAULT + url}`;

  const response = await fetch(apiUrl, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      Authorization: localStorage.getItem('access_token') || '',
    },
  });

  const responseData: T = await response.json();
  if (response.status !== 200) {
    throw new Error('API 요청에 실패했습니다.');
  }

  return responseData;
};
