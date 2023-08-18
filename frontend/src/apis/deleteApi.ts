// const API_URL =
//   process.env.NODE_ENV === 'production'
//     ? process.env.REACT_APP_API_DEFAULT_PROD
//     : process.env.REACT_APP_API_DEFAULT_DEV;

import { DEFAULT_PROD_URL } from '../constants';

export const deleteApi = async (url: string, contentType?: string) => {
  await fetch(`${DEFAULT_PROD_URL + url}`, {
    method: 'DELETE',
    headers: {
      Authorization: `Bearer ${localStorage.getItem('userToken') || ''}`,
      'Content-Type': contentType || 'application/json',
    },
  });
};
