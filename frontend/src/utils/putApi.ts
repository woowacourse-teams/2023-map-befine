export const putApi = (url: string, data: { name: string, address: string, description: string }) =>
  fetch(url, {
    method: 'PUT',
    headers: {
      'Content-type': 'application/json',
    },
    body: JSON.stringify(data),
  });
