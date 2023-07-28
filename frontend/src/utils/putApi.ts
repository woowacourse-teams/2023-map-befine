export const putApi = (url: string, data: { name: string, images: string[], description: string }) =>
  fetch(`${process.env.REACT_APP_API_DEFAULT+url}`, {
    method: 'PUT',
    headers: {
      'Content-type': 'application/json',
    },
    body: JSON.stringify(data),
  });
