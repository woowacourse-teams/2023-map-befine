export const getApi = (type: 'tMap' | 'default' | 'login', url: string) =>
  fetch(
    (type === 'tMap') || (type === 'login')
      ? url
      : `${process.env.REACT_APP_API_DEFAULT + url}`,
    {
      method: 'GET',
      headers: {
        'Content-type': 'application/json',
      },
    },
  ).then((data) => {
    return data.json();
  });
