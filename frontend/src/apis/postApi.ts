export const postApi = async (url: string, data?: {}, contentType?: string) => {
  await fetch(`${process.env.REACT_APP_API_DEFAULT + url}`, {
    method: 'POST',
    headers: {
      'Content-type': `${contentType || 'application/json'}`,
      Authorization: `Bearer ${localStorage.getItem('userToken') || ''}`,
    },
    body: JSON.stringify(data),
  });
};
