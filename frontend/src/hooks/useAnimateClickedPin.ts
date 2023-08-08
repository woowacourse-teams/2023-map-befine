import { useEffect } from 'react';

const useAnimateClickedPin = (map: any, markers: any) => {
  useEffect(() => {
    const queryParams = new URLSearchParams(location.search);
    if (queryParams.has('pinDetail')) {
      const pinId = queryParams.get('pinDetail');

      const marker = markers.find((marker: any) => marker.id === pinId);
      if (marker) {
        map.setCenter(marker.getPosition());
        map.setZoom(17);
        // marker._marker_data.options.animation =
        //   window.Tmapv3.MarkerOptions.ANIMATE_FLICKER;
        // marker._marker_data.options.animationLength = 350;
      }
    }
  }, [markers, map]);
};

export default useAnimateClickedPin;
