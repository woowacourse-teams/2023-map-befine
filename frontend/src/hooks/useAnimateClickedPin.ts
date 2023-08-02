import { useEffect } from 'react';

const useAnimateClickedPin = (map: any, markers: any) => {
  useEffect(() => {
    const queryParams = new URLSearchParams(location.search);
    if (queryParams.has('pinDetail')) {
      const pinId = queryParams.get('pinDetail');

      const marker = markers.find((marker: any) => marker.id === pinId);
      if (marker) {
        map.panTo(marker.getPosition());
        marker._marker_data.options.animation =
          window.Tmapv2.MarkerOptions.ANIMATE_BALLOON;
        marker._marker_data.options.animationLength = 500;
      }
    }
  }, [markers]);
};

export default useAnimateClickedPin;
