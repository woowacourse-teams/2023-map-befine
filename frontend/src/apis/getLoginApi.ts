export const getLoginApi = async <T>(url: string): Promise<T> => {
  const response = await fetch(url, {
    method: 'GET',
    headers: {
      'content-type': 'application/json',
    },
  });

  if (response.status >= 400) {
    throw new Error('[KAKAO] API GET 요청에 실패했습니다.');
  }

  const responseData: T = await response.json();

  return responseData;
};
