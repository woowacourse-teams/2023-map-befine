export const postApi = (url: string, data: {}) =>
  fetch(`${process.env.REACT_APP_API_DEFAULT+url}`, {
    method: 'POST',
    headers: {
      'Content-type': 'application/json',
    },
    body: JSON.stringify(data),
  });
