import { useContext, useEffect } from 'react';
import { CoordinatesContext } from '../context/CoordinatesContext';
import getAddressFromServer from '../lib/getAddressFromServer';

export default function useMapClick(map: any) {
  const { setClickedCoordinate } = useContext(CoordinatesContext);

  useEffect(() => {
    if (!map) return;
    const clickHandler = async (evt: any) => {
      const roadName = await getAddressFromServer(
        evt.data.lngLat._lat,
        evt.data.lngLat._lng,
      );
      setClickedCoordinate({
        latitude: evt.data.lngLat._lat,
        longitude: evt.data.lngLat._lng,
        address: roadName,
      });
    };
    map.on('Click', clickHandler);

    return () => {
      if (map) {
        map.removeListener('click', clickHandler);
      }
    };
  }, [map]);
}
