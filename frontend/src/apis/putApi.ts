export const putApi = async (
  url: string,
  data: { name: string; images: string[]; description: string },
) => {
  const response = await fetch(`${process.env.REACT_APP_API_DEFAULT + url}`, {
    method: 'PUT',
    headers: {
      'Content-type': 'application/json',
    },
    body: JSON.stringify(data),
  });

  if (response.status >= 400) {
    throw new Error('API 요청에 실패했습니다.');
  }

  return response;
};
