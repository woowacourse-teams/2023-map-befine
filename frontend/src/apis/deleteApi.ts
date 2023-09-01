// const API_URL =
//   process.env.NODE_ENV === 'production'
//     ? process.env.REACT_APP_API_DEFAULT_PROD
//     : process.env.REACT_APP_API_DEFAULT_DEV;

const API_URL = process.env.NODE_ENV === 'development' ? 'http://localhost:3000' : DEFAULT_PROD_URL

import { DEFAULT_PROD_URL } from '../constants';

export const deleteApi = async (url: string, contentType?: string) => {
  await fetch(`${API_URL + url}`, {
    method: 'DELETE',
    headers: {
      Authorization: `Bearer ${localStorage.getItem('userToken') || ''}`,
      'Content-Type': contentType || 'application/json',
    },
  });
};
