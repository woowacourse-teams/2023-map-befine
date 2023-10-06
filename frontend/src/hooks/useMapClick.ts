import { useContext, useEffect } from 'react';

import { CoordinatesContext } from '../context/CoordinatesContext';
import getAddressFromServer from '../lib/getAddressFromServer';
import useToast from './useToast';

export default function useMapClick(map: TMap | null) {
  const { setClickedCoordinate } = useContext(CoordinatesContext);
  const { showToast } = useToast();

  const clickHandler = async (evt: evt) => {
    try {
      const roadName = await getAddressFromServer(
        evt.data.lngLat.lat,
        evt.data.lngLat.lng,
      );

      setClickedCoordinate({
        latitude: evt.data.lngLat.lat,
        longitude: evt.data.lngLat.lng,
        address: roadName,
      });
    } catch (e) {
      showToast('error', `제공되지 않는 주소 범위입니다.`);
    }
  };

  useEffect(() => {
    if (!map) return;

    map.on('Click', clickHandler);

    return () => {
      if (map) {
        map.removeListener('click', clickHandler);
      }
    };
  }, [map]);
}
