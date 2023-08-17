import { useNavigate } from 'react-router-dom';

const useNavigator = () => {
  const navigator = useNavigate();

  const routePage = (url: string | -1, value?: string | number | number[]) => {
    if (typeof url === 'string') navigator(url, { state: value });
    if (url === -1) navigator(url);
  };

  return { routePage };
};

export default useNavigator;
