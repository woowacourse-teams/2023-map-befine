export const getAddress = (url: string) =>
  fetch(url, {
    method: 'GET',
    headers: {
      'Content-type': 'application/json',
    },
  }).then((data) => {
    return data.json();
  });
