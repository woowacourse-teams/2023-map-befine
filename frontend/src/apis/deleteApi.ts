export const deleteApi = async (url: string) => {
  await fetch(`${process.env.REACT_APP_API_DEFAULT + url}`, {
    method: 'DELETE',
    headers: {
      Authorization: `Bearer ${localStorage.getItem('userToken') || ''}`,
    },
  });
};
