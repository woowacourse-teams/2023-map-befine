import { useEffect, useState } from 'react';

import useMapStore from '../store/mapInstance';

const useAnimateClickedPin = () => {
  const queryParams = new URLSearchParams(location.search);
  const { mapInstance } = useMapStore((state) => state);
  const [checkQueryParams, setCheckQueryParams] = useState<any>(queryParams);

  const onFocusClickedPin = (markers: Marker[]) => {
    useEffect(() => {
      const currentQueryParams = new URLSearchParams(location.search);

      if (checkQueryParams === null) {
        if (!mapInstance) return;
        const pinId = queryParams.get('pinDetail');
        const marker = markers.find((marker: Marker) => marker.id === pinId);

        if (!marker) return;

        mapInstance.setCenter(marker.getPosition());

        setCheckQueryParams(currentQueryParams);
        return;
      }

      if (
        checkQueryParams.get('pinDetail') !==
        currentQueryParams.get('pinDetail')
      ) {
        const pinId = queryParams.get('pinDetail');
        const marker = markers.find((marker: Marker) => marker.id === pinId);

        if (marker && mapInstance) {
          mapInstance.setCenter(marker.getPosition());
          mapInstance.setZoom(17);
        }

        setCheckQueryParams(currentQueryParams);
      }
    }, [markers, mapInstance, queryParams]);
  };

  return { checkQueryParams, onFocusClickedPin };
};

export default useAnimateClickedPin;
