export const getMapApi = (url: string) =>
  fetch(url, {
    method: 'GET',
    headers: {
      'Content-type': 'application/json',
    },
  })
    .then((data) => {
      return data.json();
    })
    .catch((error) => {
      throw new Error(`${error.message}`);
    });
