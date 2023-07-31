export const getApi = (url: string) =>
  fetch(`${process.env.REACT_APP_API_DEFAULT+url}`, {
    method: 'GET',
    headers: {
      'Content-type': 'application/json',
    },
  }).then((data) => {
    return data.json();
  });
