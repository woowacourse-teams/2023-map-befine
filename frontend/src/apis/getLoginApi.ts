export const getLoginApi = async <T>(url: string) => {
  const response = await fetch(url, {
    method: 'GET',
    headers: {
      'content-type': 'application/json',
    },
  });

  if (response.status >= 400) {
    throw new Error('[KAKAO] GET 요청에 실패했습니다.');
  }

  const responseData: T = await response.json();

  return responseData;
};
