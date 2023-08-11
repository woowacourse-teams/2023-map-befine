import { useContext, useEffect } from 'react';
import { LayoutWidthContext } from '../context/LayoutWidthContext';

const useSetLayoutWidth = (layoutWidth: '100vw' | '400px') => {
  const { width, setWidth } = useContext(LayoutWidthContext);

  useEffect(() => {
    setWidth(layoutWidth);
  }, []);

  return { width };
};

export default useSetLayoutWidth;
