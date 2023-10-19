import { useEffect, useState } from 'react';

const useAnimateClickedPin = () => {
  const queryParams = new URLSearchParams(location.search);
  const [checkQueryParams, setCheckQueryParams] = useState<any>(queryParams);

  const onFocusClickedPin = (map: TMap | null, markers: Marker[]) => {
    useEffect(() => {
      const currentQueryParams = new URLSearchParams(location.search);

      if (checkQueryParams === null) {
        if(!map) return;
        const pinId = queryParams.get('pinDetail');
        const marker = markers.find((marker: Marker) => marker.id === pinId);

        if(!marker) return;
        map.setCenter(marker.getPosition());
        map.setZoom(17);
        setCheckQueryParams(currentQueryParams);
        return;
      }

      if (
        checkQueryParams.get('pinDetail') !==
        currentQueryParams.get('pinDetail')
      ) {
        const pinId = queryParams.get('pinDetail');
        const marker = markers.find((marker: Marker) => marker.id === pinId);

        if (marker && map) {
          map.setCenter(marker.getPosition());
          map.setZoom(17);
        }
        setCheckQueryParams(currentQueryParams);
      }
    }, [markers, map, queryParams]);
  };

  return { checkQueryParams, onFocusClickedPin };
};

export default useAnimateClickedPin;
