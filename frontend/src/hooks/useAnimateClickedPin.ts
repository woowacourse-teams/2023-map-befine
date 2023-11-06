import { useContext, useEffect, useState } from 'react';

import { CoordinatesContext } from '../context/CoordinatesContext';
import { MarkerContext } from '../context/MarkerContext';
import useMapStore from '../store/mapInstance';

const useAnimateClickedPin = () => {
  const { Tmapv3 } = window;
  const queryParams = new URLSearchParams(location.search);
  const { mapInstance } = useMapStore((state) => state);
  const [checkQueryParams, setCheckQueryParams] = useState<any>(queryParams);
  const { coordinates } = useContext(CoordinatesContext);
  const { removeMarkers, removeInfowindows, createMarkers, createInfowindows } =
    useContext(MarkerContext);

  const onFocusClickedPin = () => {
    useEffect(() => {
      const currentQueryParams = new URLSearchParams(location.search);

      // TODO : 이 부분 로직 검토해보기
      if (checkQueryParams === null) {
        if (!mapInstance) return;
        const pinId = queryParams.get('pinDetail');
        const clickedMarker = coordinates
          .map((pin: any) => {
            if (pin.pins.map((pin: any) => pin.id).includes(Number(pinId))) {
              return new Tmapv3.LatLng(pin.latitude, pin.longitude);
            }
            return null;
          })
          .find((latLng) => latLng);

        if (!clickedMarker) return;

        mapInstance.setCenter(clickedMarker);

        setCheckQueryParams(currentQueryParams);
        return;
      }

      if (
        checkQueryParams.get('pinDetail') !==
        currentQueryParams.get('pinDetail')
      ) {
        const pinId = queryParams.get('pinDetail');
        const clickedMarker = coordinates
          .map((pin: any) => {
            if (pin.pins.map((pin: any) => pin.id).includes(Number(pinId))) {
              return new Tmapv3.LatLng(pin.latitude, pin.longitude);
            }
            return null;
          })
          .find((latLng) => latLng);

        // TODO: useUpdateCoordinates 훅이랑 실행 순서 차이로 인한 업데이트 오류 있는 듯 보임. 이 훅은 sidebar 전용으로 만들어볼 것
        if (clickedMarker && mapInstance) {
          removeMarkers();
          removeInfowindows();
          mapInstance.setCenter(clickedMarker);
          mapInstance.setZoom(17);
          createMarkers();
          createInfowindows();
        }

        setCheckQueryParams(currentQueryParams);
      }
    }, [coordinates, mapInstance, queryParams.get('pinDetail')]);
  };

  return { checkQueryParams, onFocusClickedPin };
};

export default useAnimateClickedPin;
