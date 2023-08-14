import { useContext, useEffect } from 'react';
import { LayoutWidthContext } from '../context/LayoutWidthContext';

const useSetLayoutWidth = (layoutWidth: '100vw' | '372px') => {
  const { width, setWidth } = useContext(LayoutWidthContext);

  useEffect(() => {
    setWidth(layoutWidth);
  }, []);

  return { width };
};

export default useSetLayoutWidth;
