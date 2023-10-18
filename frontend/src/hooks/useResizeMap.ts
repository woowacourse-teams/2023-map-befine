import { useEffect } from 'react';

import useMapStore from '../store/mapInstance';

const getAvailableWidth = (sidebarWidth: number = 372) =>
  window.innerWidth - sidebarWidth;

const getAvailableHeight = () => window.innerHeight;

const useResizeMap = () => {
  const { mapInstance } = useMapStore((state) => state);

  const resizeMap = () => {
    if (!mapInstance) return;

    mapInstance.resize(getAvailableWidth(372), getAvailableHeight());
  };

  useEffect(() => {
    if (window.innerWidth > 1180) resizeMap();
  }, [getAvailableWidth(372)]);

  return { resizeMap };
};

export default useResizeMap;
