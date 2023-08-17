const API_URL =
  process.env.NODE_ENV === 'production'
    ? process.env.REACT_APP_API_DEFAULT_PROD
    : process.env.REACT_APP_API_DEFAULT_DEV;

interface Headers {
  'Content-Type': string;
  [key: string]: string;
}
export const putApi = async (
  url: string,
  data: { name: string; images: string[]; description: string },
) => {
  const userToken = localStorage.getItem('userToken');
  const headers: Headers = {
    'Content-Type': 'application/json',
  };
  if (userToken) {
    headers['Authorization'] = `Bearer ${userToken}`;
  }

  const response = await fetch(`${API_URL + url}`, {
    method: 'PUT',
    headers: headers,
    body: JSON.stringify(data),
  });
  if (response.status >= 400) {
    throw new Error('API 요청에 실패했습니다.');
  }
  return response;
};
