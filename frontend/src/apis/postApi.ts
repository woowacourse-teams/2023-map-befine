interface Headers {
  'Content-Type': string;
  [key: string]: string;
}

export const postApi = async (url: string, data?: {}, contentType?: string) => {
  const userToken = localStorage.getItem('userToken');
  const headers: Headers = {
   'Content-type': `${contentType || 'application/json'}`,
  };

  if (userToken) {
    headers['Authorization'] = `Bearer ${userToken}`;
  }

  const response = await fetch(`${process.env.REACT_APP_API_DEFAULT + url}`, {
    method: 'POST',
    headers: headers,
    body: JSON.stringify(data),
  });

  return response;
};
